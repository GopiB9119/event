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
            SAMPLE MERCHANT
            5,000.00
            25 Jun 2026
            """.trimIndent()
        )

        assertEquals("PhonePe", parsed.paymentApp)
        assertEquals(5000.0, parsed.amount, 0.01)
        assertEquals("SAMPLE MERCHANT", parsed.counterpartyName)
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
    fun `sanitized google pay ocr keeps sender counterparty instead of app name`() {
        val parsed = ReceiptParser.parse(
            """
            From SAMPLE SENDER
            50
            Payment from PhonePe
            22 Jul 2025, 4:31 pm
            UPI transaction ID
            123456789012
            Completed
            Sample Bank 0001
            To: SAMPLE RECEIVER
            Google Pay • receiver.sample@oksbi
            From: SAMPLE SENDER
            PhonePe • sender.sample@ibl
            Google transaction ID
            SAMPLEGOOGLE123
            G Pay
            """.trimIndent()
        )

        assertEquals("Google Pay", parsed.paymentApp)
        assertEquals(50.0, parsed.amount, 0.01)
        assertEquals("123456789012", parsed.transactionId)
        assertEquals("SAMPLE SENDER", parsed.counterpartyName)
        assertEquals("receiver.sample@oksbi", parsed.upiId)
    }

    @Test
    fun `sanitized phonepe ocr ignores transaction id digits as amount`() {
        val parsed = ReceiptParser.parse(
            """
            Transaction Successful
            09:27 am on 21 Jun 2026
            Received from
            SAMPLE MEMBER
            Banking Name : SAMPLE BANK NAME
            PhonePe Transaction ID
            T1234567890123456789012
            Credited to
            XXXXXX0001@sbi
            UTR: 123456789013
            2,000
            """.trimIndent()
        )

        assertEquals("PhonePe", parsed.paymentApp)
        assertEquals(2000.0, parsed.amount, 0.01)
        assertEquals("123456789012345678", parsed.transactionId)
        assertEquals("SAMPLE MEMBER", parsed.counterpartyName)
    }

    @Test
    fun `sanitized samsung wallet ocr prefers visible amount over upi handle digits`() {
        val parsed = ReceiptParser.parse(
            """
            Sent from:
            merchant.synthetic@cashfree
            Transaction ID:
            Paid from
            Notes:
            Samsung Wallet
            sdlpb
            01 JUL 2026, 09:54 AM
            18.50
            Sent
            1234567890 @pingpay
            123456789012
            Powered by AXIS BANK | LI>
            UNIFIED PAYMENTS INTERFACE
            UPI
            718.50
            1234567890@pingpay
            """.trimIndent()
        )

        assertEquals("Samsung Pay", parsed.paymentApp)
        assertEquals(18.50, parsed.amount, 0.01)
        assertEquals("123456789012", parsed.transactionId)
        assertEquals("merchant.synthetic@cashfree", parsed.upiId)
    }

    @Test
    fun `sanitized samsung wallet ocr finds delayed labelled transaction id`() {
        val parsed = ReceiptParser.parse(
            """
            Sent from:
            Transaction ID:
            Notes:
            Paid from
            Samsung Wallet
            SAMPLE MERCHANT
            1234567890@naviaxis
            02 JUL 2026, 08:22 AM
            1,000.00
            Sent
            1234567890 @pingpay
            123456789013
            Powered by AXIS BANK | LI>
            UNIFIED PAYMENTS INTERFACE
            UPI
            """.trimIndent()
        )

        assertEquals("Samsung Pay", parsed.paymentApp)
        assertEquals(1000.0, parsed.amount, 0.01)
        assertEquals("123456789013", parsed.transactionId)
        assertEquals("1234567890@naviaxis", parsed.upiId)
    }
}