package com.github.radlance.kanbanboards.auth.presentation

import com.github.radlance.kanbanboards.auth.presentation.common.MatchEmail
import com.github.radlance.kanbanboards.auth.presentation.common.ValidateAuth
import com.github.radlance.kanbanboards.common.BaseTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateAuthTest : BaseTest() {

    private lateinit var matchEmail: TestMatchEmail
    private lateinit var manageResource: TestManageResource

    private lateinit var validateAuth: ValidateAuth

    @Before
    fun setup() {
        matchEmail = TestMatchEmail()
        manageResource = TestManageResource()

        validateAuth = ValidateAuth.Base(manageResource, matchEmail)
    }

    @Test
    fun test_name_blank() {
        manageResource.makeExpectedString(expected = "name is blank")

        assertEquals("name is blank", validateAuth.validName(value = ""))
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_valid_name() {
        assertEquals("", validateAuth.validName(value = "some name"))
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_email_no_match() {
        manageResource.makeExpectedString(expected = "invalid email")
        matchEmail.makeExpectedMatch(match = false)

        assertEquals("invalid email", validateAuth.validEmail(value = "wrong email"))
        assertEquals(1, manageResource.stringCalledCount)
        assertEquals(1, matchEmail.matchCalledCount)
    }

    @Test
    fun test_email_blank() {
        manageResource.makeExpectedString(expected = "email is blank")
        matchEmail.makeExpectedMatch(match = true)

        assertEquals("email is blank", validateAuth.validEmail(value = ""))
        assertEquals(1, manageResource.stringCalledCount)
        assertEquals(1, matchEmail.matchCalledCount)
    }

    @Test
    fun test_valid_email() {
        matchEmail.makeExpectedMatch(match = true)
        assertEquals("", validateAuth.validEmail(value = "email@test.com"))
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_password_short() {
        manageResource.makeExpectedString(expected = "min password length is 6")
        assertEquals("min password length is 6", validateAuth.validPassword(value = "12345"))
    }

    @Test
    fun test_password_blank() {
        manageResource.makeExpectedString(expected = "password is blank")

        assertEquals("password is blank", validateAuth.validPassword(value = ""))
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_valid_password() {
        assertEquals("", validateAuth.validPassword(value = "some password"))
        assertEquals(0, manageResource.stringCalledCount)
    }

    @Test
    fun test_password_confirm_blank() {
        manageResource.makeExpectedString(expected = "confirm password is blank")

        assertEquals(
            "confirm password is blank",
            validateAuth.validPasswordConfirm(confirm = "", password = "123456")
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_password_confirm_not_match() {
        manageResource.makeExpectedString(expected = "passwords don't match")

        assertEquals(
            "passwords don't match",
            validateAuth.validPasswordConfirm(confirm = "987654", password = "123456")
        )
        assertEquals(1, manageResource.stringCalledCount)
    }

    @Test
    fun test_valid_password_confirm() {
        assertEquals(
            "",
            validateAuth.validPasswordConfirm(confirm = "987654", password = "987654")
        )
        assertEquals(0, manageResource.stringCalledCount)
    }


    private class TestMatchEmail : MatchEmail {

        private var match = false
        var matchCalledCount = 0

        fun makeExpectedMatch(match: Boolean) {
            this.match = match
        }

        override fun match(value: String): Boolean {
            matchCalledCount++
            return match
        }
    }
}