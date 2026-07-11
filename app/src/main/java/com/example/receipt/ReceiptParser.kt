package com.example.receipt

import java.util.regex.Pattern

data class ParsedReceipt(
    val amount: Double = 0.0,
    val transactionId: String = "",
    val phone: String = "",
    val email: String = "",
    val payeeName: String = "",
    val upiId: String = "",
    val date: String = "",
    val paymentApp: String = "Unknown UPI",
    val extractionMethod: String = "No receipt data found",
    val rawTextPreview: String = "",
    val confidence: Int = 0,
    val validationWarnings: List<String> = emptyList()
)

object ReceiptParser {
    fun parse(text: String): ParsedReceipt {
        var amount = 0.0
        var transactionId = ""
        var phone = ""
        var email = ""
        var payeeName = ""
        var upiId = ""
        var dateStr = ""
        var paymentApp = "Unknown UPI"
        var amountConfidence = 0
        val lines = text.lineSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()

        fun parseAmountValue(value: String): Double? {
            val digitsOnly = value.replace(Regex("\\D"), "")
            if (digitsOnly.length >= 10) return null

            val parsed = value.replace(" ", "").replace(",", "").toDoubleOrNull()
            if (parsed != null && digitsOnly.length == 4 && parsed.toInt() in 2020..2035) return null
            return parsed?.takeIf { it >= 1.0 && it <= 150000.0 }
        }

        fun cleanReferenceId(value: String): String {
            return value.replace(Regex("[^A-Za-z0-9]"), "")
        }

        fun firstNonBlankGroup(matcher: java.util.regex.Matcher): String {
            for (index in 1..matcher.groupCount()) {
                val group = matcher.group(index)
                if (!group.isNullOrBlank()) return group
            }
            return ""
        }

        fun referenceCandidateFrom(value: String, allowShortNumeric: Boolean = false): String? {
            val cleanValue = cleanReferenceId(value)
            val digitCount = cleanValue.count { it.isDigit() }
            if (cleanValue.length !in 10..32) return null
            if (digitCount < 6) return null
            if (cleanValue.all { it.isDigit() } && cleanValue.length == 10 && !allowShortNumeric) return null
            return cleanValue
        }

        fun findReferenceCandidate(segment: String, allowShortNumeric: Boolean = false): String? {
            val numericMatcher = Pattern.compile("((?:\\d[\\s-]?){12,18})").matcher(segment)
            if (numericMatcher.find()) {
                referenceCandidateFrom(numericMatcher.group(1) ?: "")?.let { return it }
            }

            val alphaNumericMatcher = Pattern.compile("\\b([A-Za-z0-9][A-Za-z0-9\\s-]{9,32})\\b").matcher(segment)
            while (alphaNumericMatcher.find()) {
                referenceCandidateFrom(alphaNumericMatcher.group(1) ?: "", allowShortNumeric)?.let { return it }
            }

            return null
        }

        fun isAmountLikeLine(line: String): Boolean {
            return parseAmountValue(line.replace(Regex("[^0-9, .]"), "")) != null
        }

        fun isDateLikeLine(line: String): Boolean {
            return Pattern.compile("\\b\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4}\\b|\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b").matcher(line).find()
        }

        fun containsToken(value: String, tokenPattern: String): Boolean {
            return Pattern.compile(tokenPattern, Pattern.CASE_INSENSITIVE).matcher(value).find()
        }

        val referenceLabelPattern = Pattern.compile(
            "\\b(?:upi\\s*(?:transaction|txn)?\\s*(?:id|ref(?:erence)?(?:\\s*no)?|number)|transaction\\s*(?:id|no|number)|txn\\s*(?:id|no|number)|utr|bank\\s*ref(?:erence)?|approval\\s*(?:code|number|no)|auth(?:orization)?\\s*(?:code|number|no)|order\\s*(?:id|number|no)|receipt\\s*(?:id|number|no)|ref(?:erence)?\\s*(?:id|number|no)?)\\b",
            Pattern.CASE_INSENSITIVE
        )

        fun isNumberNoiseLine(line: String): Boolean {
            val lowerLine = line.lowercase()
            val digitCount = line.count { it.isDigit() }
            return line.contains("@") ||
                digitCount >= 10 ||
                referenceLabelPattern.matcher(line).find() ||
                containsToken(lowerLine, "\\b(?:account|card|bank|phone|mobile|contact|balance|powered|axis)\\b") ||
                containsToken(line, "(?:\\+91[-\\s]?)?[6-9][\\d\\s-]{9,14}")
        }

            fun referencePriority(labelLine: String): Int {
                val lowerLabelLine = labelLine.lowercase()
                return when {
                    containsToken(lowerLabelLine, "\\bphonepe\\s+transaction\\s+id\\b") -> 0
                    containsToken(lowerLabelLine, "\\b(?:upi\\s*(?:transaction|txn)?\\s*id|transaction\\s*id|txn\\s*id)\\b") -> 1
                    containsToken(lowerLabelLine, "\\butr\\b") -> 2
                    else -> 3
                }
            }

            fun isReferenceNoiseLine(line: String): Boolean {
                val lowerLine = line.lowercase()
                val looksExplicitlyLikePhone = containsToken(line, "\\+91[-\\s]?[6-9][\\d\\s-]{9,14}") ||
                    containsToken(lowerLine, "\\b(?:phone|mobile|contact)\\b")
                return line.contains("@") ||
                isAmountLikeLine(line) ||
                isDateLikeLine(line) ||
                    looksExplicitlyLikePhone ||
                    containsToken(lowerLine, "\\b(?:paid|sent|from|to|notes|bank|axis|powered|unified|payments|interface|phonepe|google\\s+pay|gpay|samsung\\s+wallet|samsung\\s+pay|paytm|completed|successful|success|transfer|details|amount)\\b")
            }

        fun cleanPartyCandidate(value: String): String? {
            val candidate = value
                .replace(Regex("^(?:from|to|paid\\s+to|sent\\s+to|transferred\\s+to|received\\s+from|merchant|payee|recipient|banking\\s+name)\\s*:?\\s*", RegexOption.IGNORE_CASE), "")
                .trim()
                .trim('-', ':', '•')
                .trim()
            val lowerCandidate = candidate.lowercase()
            if (candidate.length < 2) return null
            if (candidate.contains("@")) return null
            if (isAmountLikeLine(candidate) || isDateLikeLine(candidate)) return null
            if (containsToken(lowerCandidate, "\\b(?:completed|successful|success|transfer|details|transaction|utr|upi|unified|powered|bank|notes|paid\\s+from|sent|payment|phonepe|google\\s+pay|gpay|paytm|samsung\\s+wallet|samsung\\s+pay)\\b")) return null
            return candidate
        }

        val lowerText = text.lowercase()
        paymentApp = when {
            containsToken(lowerText, "\\bsamsung\\s+(?:wallet|pay)\\b|\\bsamsungpay\\b") -> "Samsung Pay"
            containsToken(lowerText, "\\bgoogle\\s+transaction\\s+id\\b|\\bgoogle\\s+pay\\b|\\bg\\s*pay\\b|\\bgpay\\b|\\btez\\b") -> "Google Pay"
            containsToken(lowerText, "\\bphonepe\\b") -> "PhonePe"
            containsToken(lowerText, "\\bpaytm\\b") -> "Paytm"
            containsToken(lowerText, "\\bamazon\\s*pay\\b") -> "Amazon Pay"
            containsToken(lowerText, "\\bbhim\\b") -> "BHIM"
            containsToken(lowerText, "\\bcred\\b") -> "CRED"
            containsToken(lowerText, "\\bwhatsapp(?:\\s+pay)?\\b") -> "WhatsApp Pay"
            containsToken(lowerText, "\\bmobikwik\\b") -> "MobiKwik"
            containsToken(lowerText, "\\bfreecharge\\b") -> "Freecharge"
            containsToken(lowerText, "\\bsuper\\.?(?:money)\\b") -> "Super.money"
            containsToken(lowerText, "\\bping\\s*pay\\b") -> "Ping Pay"
            else -> "Unknown UPI"
        }

        if (paymentApp == "Unknown UPI") {
            val appFromPackage = Regex("\\b(com\\.[a-z0-9_.-]*(?:phonepe|google|paytm|amazon|samsung|cred|mobikwik|freecharge)[a-z0-9_.-]*)\\b", RegexOption.IGNORE_CASE)
                .find(text)
                ?.value
                .orEmpty()
                .lowercase()
            paymentApp = when {
                "phonepe" in appFromPackage -> "PhonePe"
                "google" in appFromPackage -> "Google Pay"
                "paytm" in appFromPackage -> "Paytm"
                "amazon" in appFromPackage -> "Amazon Pay"
                "samsung" in appFromPackage -> "Samsung Pay"
                "cred" in appFromPackage -> "CRED"
                "mobikwik" in appFromPackage -> "MobiKwik"
                "freecharge" in appFromPackage -> "Freecharge"
                else -> "Unknown UPI"
            }
        }

        data class AmountCandidate(val value: Double, val score: Int, val order: Int)
        val amountCandidates = mutableListOf<AmountCandidate>()
        var amountCandidateOrder = 0
        val amountNumberPattern = "([0-9]{1,3}(?:[ ,][0-9]{2,3})+(?:\\.\\d{1,2})?|[0-9]{1,6}(?:\\.\\d{1,2})?)"
        val currencyAmountPattern = Pattern.compile("(?:₹|Rs\\.?|INR)\\s*$amountNumberPattern|$amountNumberPattern\\s*(?:₹|Rs\\.?|INR)", Pattern.CASE_INSENSITIVE)
        val looseAmountPattern = Pattern.compile(amountNumberPattern)
        val standaloneAmountPattern = Pattern.compile("^(?:₹|Rs\\.?|INR)?\\s*$amountNumberPattern\\s*(?:₹|Rs\\.?|INR)?$", Pattern.CASE_INSENSITIVE)
        val amountLabelPattern = Pattern.compile("\\b(?:paid|sent|amount|transfer|total|received|debited|credited|purchase|payment|transaction\\s+amount)\\b", Pattern.CASE_INSENSITIVE)

        fun addAmountCandidate(rawValue: String, score: Int, sourceLine: String) {
            if (isNumberNoiseLine(sourceLine)) return
            val parsed = parseAmountValue(rawValue) ?: return
            amountCandidates += AmountCandidate(parsed, score, amountCandidateOrder++)
        }

        lines.forEachIndexed { index, line ->
            val currencyMatcher = currencyAmountPattern.matcher(line)
            while (currencyMatcher.find()) {
                addAmountCandidate(firstNonBlankGroup(currencyMatcher), 100, line)
            }

            val standaloneMatcher = standaloneAmountPattern.matcher(line)
            if (standaloneMatcher.find()) {
                val score = if (index <= 4) 90 else 55
                addAmountCandidate(firstNonBlankGroup(standaloneMatcher), score, line)
            }

            if (amountLabelPattern.matcher(line).find()) {
                val lineMatcher = looseAmountPattern.matcher(line)
                while (lineMatcher.find()) {
                    addAmountCandidate(firstNonBlankGroup(lineMatcher), 80, line)
                }

                lines.drop(index + 1).take(2).forEach { nearbyLine ->
                    val nearbyCurrencyMatcher = currencyAmountPattern.matcher(nearbyLine)
                    var foundCurrencyNearby = false
                    while (nearbyCurrencyMatcher.find()) {
                        foundCurrencyNearby = true
                        addAmountCandidate(firstNonBlankGroup(nearbyCurrencyMatcher), 95, nearbyLine)
                    }

                    if (!foundCurrencyNearby) {
                        val nearbyNumberMatcher = looseAmountPattern.matcher(nearbyLine)
                        while (nearbyNumberMatcher.find()) {
                            addAmountCandidate(firstNonBlankGroup(nearbyNumberMatcher), 65, nearbyLine)
                        }
                    }
                }
            }
        }

        val selectedAmountCandidate = amountCandidates
            .sortedWith(compareByDescending<AmountCandidate> { it.score }.thenBy { it.order })
            .firstOrNull()
        amount = selectedAmountCandidate?.value ?: 0.0
        amountConfidence = selectedAmountCandidate?.score ?: 0

        data class ReferenceCandidate(val value: String, val priority: Int, val order: Int)
        val referenceCandidates = mutableListOf<ReferenceCandidate>()

        for (index in lines.indices) {
            val line = lines[index]
            if (referenceLabelPattern.matcher(line).find()) {
                val labelMatcher = referenceLabelPattern.matcher(line)
                val afterLabel = if (labelMatcher.find()) line.substring(labelMatcher.end()) else line
                val priority = referencePriority(line)
                val directCandidate = findReferenceCandidate(afterLabel, allowShortNumeric = true)
                if (directCandidate != null) {
                    referenceCandidates += ReferenceCandidate(directCandidate, priority, index)
                } else {
                    val nearbyCandidate = lines.drop(index + 1).take(10)
                        .firstNotNullOfOrNull { nearbyLine ->
                            if (isReferenceNoiseLine(nearbyLine)) return@firstNotNullOfOrNull null
                            findReferenceCandidate(nearbyLine, allowShortNumeric = true)
                        }
                    if (nearbyCandidate != null) {
                        referenceCandidates += ReferenceCandidate(nearbyCandidate, priority, index)
                    }
                }
            }
        }
        transactionId = referenceCandidates
            .sortedWith(compareBy<ReferenceCandidate> { it.priority }.thenBy { it.order })
            .firstOrNull()
            ?.value
            .orEmpty()

        val phonePattern = Pattern.compile("(?:Phone|Mobile|Contact)?[:\\s]*(?:\\+91[-\\s]?)?([6-9][\\d\\s-]{9,14})", Pattern.CASE_INSENSITIVE)
        val phoneMatcher = phonePattern.matcher(text)
        if (phoneMatcher.find()) {
            val cleanPhone = (phoneMatcher.group(1) ?: "").replace(Regex("\\D"), "")
            if (cleanPhone.length == 10) {
                phone = cleanPhone
            }
        }

        val emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})")
        val emailMatcher = emailPattern.matcher(text)
        if (emailMatcher.find()) {
            email = emailMatcher.group(1) ?: ""
        }

        val upiMatcher = Pattern.compile("\\b([a-zA-Z0-9._-]{2,}@[a-zA-Z][a-zA-Z0-9._-]{1,})\\b").matcher(text)
        if (upiMatcher.find()) {
            upiId = upiMatcher.group(1) ?: ""
        }

        val receivedFromPattern = Pattern.compile("\\b(?:received\\s+from|from)\\b", Pattern.CASE_INSENSITIVE)
        val paidToPattern = Pattern.compile("\\b(?:paid|sent|transferred)\\s+to\\b|\\b(?:merchant|payee|recipient|to)\\b", Pattern.CASE_INSENSITIVE)
        val partyPatterns = listOf(receivedFromPattern, paidToPattern)
        for (index in lines.indices) {
            val line = lines[index]
            val matchedPattern = partyPatterns.firstOrNull { pattern -> pattern.matcher(line).find() }
            if (matchedPattern != null) {
                val matcher = matchedPattern.matcher(line)
                val sameLineCandidate = if (matcher.find()) cleanPartyCandidate(line.substring(matcher.end())) else null
                val nearbyCandidate = lines.drop(index + 1)
                    .take(3)
                    .firstNotNullOfOrNull { candidate -> cleanPartyCandidate(candidate) }
                payeeName = sameLineCandidate ?: nearbyCandidate.orEmpty()
                if (payeeName.isNotBlank()) break
            }
        }

        val datePatterns = listOf(
            Pattern.compile("(\\d{1,2}\\s+[A-Za-z]{3,9}\\s+\\d{4})"),
            Pattern.compile("([A-Za-z]{3,9}\\s+\\d{1,2},?\\s+\\d{4})"),
            Pattern.compile("(\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4})")
        )
        for (pattern in datePatterns) {
            val matcher = pattern.matcher(text)
            if (matcher.find()) {
                dateStr = matcher.group(1) ?: ""
                break
            }
        }

        val warnings = mutableListOf<String>()
        if (amount <= 0.0) warnings += "Amount not detected; ledger calculation is blocked."
        if (amountConfidence in 1..69) warnings += "Amount was detected with low OCR confidence."
        if (paymentApp == "Unknown UPI") warnings += "Payment app not detected."
        if (transactionId.isBlank()) warnings += "UPI reference or transaction ID not detected."
        if (payeeName.isBlank() && upiId.isBlank()) warnings += "Receiver name or UPI ID not detected."

        val confidence = listOf(
            if (amount > 0.0) when {
                amountConfidence >= 95 -> 45
                amountConfidence >= 80 -> 38
                amountConfidence >= 65 -> 30
                else -> 20
            } else 0,
            if (paymentApp != "Unknown UPI") 15 else 0,
            if (transactionId.isNotBlank()) 20 else 0,
            if (upiId.isNotBlank()) 10 else 0,
            if (payeeName.isNotBlank()) 5 else 0,
            if (dateStr.isNotBlank()) 5 else 0
        ).sum().coerceIn(0, 100)

        return ParsedReceipt(
            amount = amount,
            transactionId = transactionId,
            phone = phone,
            email = email,
            payeeName = payeeName,
            upiId = upiId,
            date = dateStr,
            paymentApp = paymentApp,
            confidence = confidence,
            validationWarnings = warnings
        )
    }
}