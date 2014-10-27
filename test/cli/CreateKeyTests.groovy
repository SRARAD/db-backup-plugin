import grails.test.AbstractCliTestCase

class CreateKeyTests extends AbstractCliTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testCreateKey() {

        execute(["create-key"])

        assertEquals 0, waitForProcess()
        verifyHeader()
    }
}
