package com.example.receipt

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReceiptParserTest {
    @Test
    fun `parses labelled amount reference app and date`() {
        val parsed = ReceiptParser.parse(
            """
            Google Pay
            Paid ₹1,500.00
            UPI Ref No: 310725987654
            25 Jun 2026
            """.trimIndent()
        )

        assertEquals("Google Pay", parsed.paymentApp)
        assertEquals(1500.0, parsed.amount, 0.01)
        assertEquals("310725987654", parsed.transactionId)
        assertEquals("25 Jun 2026", parsed.date)
        assertTrue(parsed.confidence >= 80)
    }

    @Test
    fun `parses standalone top receipt amount and payee`() {
        val parsed = ReceiptParser.parse(
            """
            PhonePe
            Paid to
            ALLI RAMARAO
            5,000.00
            25 Jun 2026
            """.trimIndent()
        )

        assertEquals("PhonePe", parsed.paymentApp)
        assertEquals(5000.0, parsed.amount, 0.01)
        assertEquals("ALLI RAMARAO", parsed.payeeName)
        assertEquals("25 Jun 2026", parsed.date)
        assertTrue(parsed.validationWarnings.contains("UPI reference or transaction ID not detected."))
    }

    @Test
    fun `does not treat unlabelled long numbers as reference ids`() {
        val parsed = ReceiptParser.parse(
            """
            Paytm
            Amount INR 250
            Account 987654321012
            Balance 4300
            24/06/2026
            """.trimIndent()
        )

        assertEquals("Paytm", parsed.paymentApp)
        assertEquals(250.0, parsed.amount, 0.01)
        assertEquals("", parsed.transactionId)
        assertTrue(parsed.validationWarnings.contains("UPI reference or transaction ID not detected."))
    }

    @Test
    fun `missing amount remains blocked and null-like`() {
        val parsed = ReceiptParser.parse(
            """
            PhonePe
            UPI Ref No: 310725987654
            Paid to
            Some Merchant
            """.trimIndent()
        )

        assertEquals(0.0, parsed.amount, 0.01)
        assertEquals("310725987654", parsed.transactionId)
        assertTrue(parsed.confidence < 60)
        assertTrue(parsed.validationWarnings.contains("Amount not detected; ledger calculation is blocked."))
    }

    @Test
    fun `real google pay ocr keeps receiver instead of sender app`() {
        val parsed = ReceiptParser.parse(
            """
            From REGALLA SAI KIRAN
            50
            Payment from PhonePe
            22 Jul 2025, 4:31 pm
            UPI transaction ID
            938718462699
            Completed
            State Bank of India 7677
            To: BANOTH GOPI
            Google Pay• banothgopikrishna19-5@oksbi
            From: REGALLA SAI KIRAN
            PhonePe • regallasaikiran1@ibl
            Google transaction ID
            CICAgkjpmOCHew
            G Pay
            """.trimIndent()
        )

        assertEquals("Google Pay", parsed.paymentApp)
        assertEquals(50.0, parsed.amount, 0.01)
        assertEquals("938718462699", parsed.transactionId)
        assertEquals("REGALLA SAI KIRAN", parsed.payeeName)
        assertEquals("banothgopikrishna19-5@oksbi", parsed.upiId)
    }

    @Test
    fun `real phonepe ocr ignores transaction id digits as amount`() {
        val parsed = ReceiptParser.parse(
            """
            Transaction Successful
            09:27 am on 21 Jun 2026
            Received from
            Naresh Srt
            +918639231893
            Banking Name : Dharavath Naresh
            PhonePe Transaction ID
            T2606210927085344132760
            Credited to
            XXXXXXO621@sbi
            UTR: 091504776352
            2,000
            """.trimIndent()
        )

        assertEquals("PhonePe", parsed.paymentApp)
        assertEquals(2000.0, parsed.amount, 0.01)
        assertEquals("260621092708534413", parsed.transactionId)
        assertEquals("Naresh Srt", parsed.payeeName)
    }

    @Test
    fun `real samsung wallet ocr prefers visible amount over upi handle digits`() {
        val parsed = ReceiptParser.parse(
            """
            Sent from:
            cf.innovativeretailconcepts@cashfreen
            Transaction ID:
            Paid from
            Notes:
            Samsung Wallet
            sdlpb
            01 JUL 2026, 09:54 AM
            18.50
            Sent
            7981710621 @pingpay
            726202791826
            Powered by AXIS BANK | LI>
            UNIFIED PAYMENTS INTERFACE
            UPI
            718.50
            7981710621@pingpay
            """.trimIndent()
        )

        assertEquals("Samsung Pay", parsed.paymentApp)
        assertEquals(18.50, parsed.amount, 0.01)
        assertEquals("726202791826", parsed.transactionId)
        assertEquals("cf.innovativeretailconcepts@cashfreen", parsed.upiId)
    }

    @Test
    fun `real samsung wallet ocr finds delayed labelled transaction id`() {
        val parsed = ReceiptParser.parse(
            """
            Sent from:
            Transaction ID:
            Notes:
            Paid from
            Samsung Wallet
            DANISH
            7330865196@naviaxis
            02 JUL 2026, 08:22 AM
            1,000.00
            Sent
            7981710621 @pingpay
            922904561836
            Powered by AXIS BANK | LI>
            UNIFIED PAYMENTS INTERFACE
            UPI
            """.trimIndent()
        )

        assertEquals("Samsung Pay", parsed.paymentApp)
        assertEquals(1000.0, parsed.amount, 0.01)
        assertEquals("922904561836", parsed.transactionId)
        assertEquals("7330865196@naviaxis", parsed.upiId)
    }
}