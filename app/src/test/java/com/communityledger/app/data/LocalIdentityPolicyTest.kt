package com.communityledger.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LocalIdentityPolicyTest {
    @Test
    fun `normalizes a valid local identity`() {
        assertEquals("member@example.com", normalizeLocalIdentity("  Member@Example.COM  "))
    }

    @Test
    fun `rejects blank and malformed local identities`() {
        assertNull(normalizeLocalIdentity(""))
        assertNull(normalizeLocalIdentity("member"))
        assertNull(normalizeLocalIdentity("member@example"))
        assertNull(normalizeLocalIdentity("member @example.com"))
    }
}