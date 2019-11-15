package com.darthShana.encrypt

import spock.lang.Specification

class AzureEnvelopKeyManagerTest extends Specification {

    def "Encrypt"() {
        given:
        def keyManager = new AzureEnvelopKeyManager()
        keyManager.initialise()
        when:
        def encrypted = keyManager.encrypt("some stuff to encrypt")
        and:
        def decrypted = keyManager.decrypt(encrypted)
        then:
        decrypted == "some stuff to encrypt"
        encrypted != decrypted
    }
}
