package org.kethereum.eip712

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.kethereum.contract.abi.types.model.type_params.BitsTypeParams
import org.kethereum.contract.abi.types.model.types.*
import org.kethereum.crypto.api.ec.ECDSASignature
import org.kethereum.crypto.impl.ec.EllipticCurveSigner
import org.kethereum.crypto.signMessage
import org.kethereum.crypto.signMessageHash
import org.kethereum.crypto.toAddress
import org.kethereum.crypto.toECKeyPair
import org.kethereum.extensions.hexToBigInteger
import org.kethereum.extensions.toBigInteger
import org.kethereum.keccakshortcut.keccak
import org.kethereum.model.Address
import org.kethereum.model.PrivateKey
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.extensions.toHexString
import org.komputing.khex.extensions.toNoPrefixHexString
import org.komputing.khex.model.HexString
import pm.gnosis.eip712.*
import java.math.BigDecimal.ONE
import java.math.BigInteger
import java.security.KeyPair
import kotlin.test.assertEquals


private val TEST_DOMAIN = Struct712(
    typeName = EIP712_DOMAIN_TYPE,
    parameters = listOf(
        "name" asParameterNameFor Literal712(typeName = "string", value = StringETHType.ofNativeKotlinType("Ether Mail")),
        "version" asParameterNameFor Literal712(typeName = "string", value = StringETHType.ofNativeKotlinType("1")),
        "chainId" asParameterNameFor Literal712(typeName = "uint256", value = UIntETHType.ofNativeKotlinType(BigInteger.ONE, BitsTypeParams(256))),
        "verifyingContract" asParameterNameFor Literal712(
            typeName = "address",
            value = AddressETHType.ofNativeKotlinType(Address("0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC"))
        )
    )
)

private val FROM_PERSON = Struct712(
    typeName = "Person", parameters = listOf(
        "name" asParameterNameFor Literal712(typeName = "string", value = StringETHType.ofNativeKotlinType("Cow")),
        "wallet" asParameterNameFor Literal712(
            typeName = "address",
            value = AddressETHType.ofNativeKotlinType(Address("0xCD2a3d9F938E13CD947Ec05AbC7FE734Df8DD826"))
        )
    )
)

private val TO_PERSON = Struct712(
    typeName = "Person", parameters = listOf(
        "name" asParameterNameFor Literal712(typeName = "string", value = StringETHType.ofNativeKotlinType("Bob")),
        "wallet" asParameterNameFor Literal712(
            typeName = "address",
            value = AddressETHType.ofNativeKotlinType(Address("0xbBbBBBBbbBBBbbbBbbBbbbbBBbBbbbbBbBbbBBbB"))
        )
    )
)

private val CONTENTS = Literal712(typeName = "string", value = StringETHType.ofNativeKotlinType(("Hello, Bob!")))

private val PAYLOAD =
    Literal712(
        typeName = "bytes",
        value = DynamicSizedBytesETHType.ofNativeKotlinType(HexString("0x25192142931f380985072cdd991e37f65cf8253ba7a0e675b54163a1d133b8ca").hexToByteArray())
    )

private val PERSONS_ARRAY =
    Array712(
        typeName = "Person[]",
        items = listOf(TO_PERSON, TO_PERSON)
    )

class TheEIP712 {

    @Test
    fun eip712() {
        val mail = Struct712(
            typeName = "Mail",
            parameters = listOf(
                "from" asParameterNameFor FROM_PERSON,
                "to" asParameterNameFor TO_PERSON,
                "contents" asParameterNameFor CONTENTS
            )
        )
        assertEquals(
            "8b73c3c69bb8fe3d512ecc4cf759cc79239f7b179b0ffacaa9a75d522b39400f",
            TEST_DOMAIN.typeHash.toNoPrefixHexString()
        )

        assertEquals(
            "a0cedeb2dc280ba39b857546d74f5549c3a1d7bdc2dd96bf881f76108e23dac2",
            mail.typeHash.toNoPrefixHexString()
        )

        assertEquals(
            "b9d8c78acf9b987311de6c7b45bb6a9c8e1bf361fa7fd3467a2163f994c79500",
            TO_PERSON.typeHash.toNoPrefixHexString()
        )

        assertEquals(
            "c52c0ee5d84264471806290a3f2c4cecfc5490626bf912d01f240d7a274b371e",
            mail.hashStruct().toNoPrefixHexString()
        )

        assertEquals(
            "f2cee375fa42b42143804025fc449deafd50cc031ca257e0b194a650a912090f",
            TEST_DOMAIN.hashStruct().toNoPrefixHexString()
        )

    }

    @Test
    fun eip712Bytes() {
        val mail = Struct712(
            typeName = "Mail",
            parameters = listOf(
                "from" asParameterNameFor FROM_PERSON,
                "to" asParameterNameFor TO_PERSON,
                "contents" asParameterNameFor CONTENTS,
                "payload" asParameterNameFor PAYLOAD
            )
        )

        assertEquals(
            "43999c52db673245777eb64b0330105de064e52179581a340a9856c32372528e",
            mail.typeHash.toNoPrefixHexString()
        )

        assertEquals(
            "e004bdc1ca57ba9ad5ea8c81e54dcbdb3bfce2d1d5ad92113f0871fb2a6eb052",
            mail.hashStruct().toNoPrefixHexString()
        )
    }

    @Test
    fun eip712Arrays() {
        val mail = Struct712(
            typeName = "Mail",
            parameters = listOf(
                "from" asParameterNameFor FROM_PERSON,
                "to" asParameterNameFor PERSONS_ARRAY,
                "contents" asParameterNameFor CONTENTS,
                "payload" asParameterNameFor PAYLOAD
            )
        )

        assertEquals(
            "3dddc94d13b9ebab8e68f1428610e81839fcd751bdee402b12d2b3de3aace1fd",
            mail.typeHash.toNoPrefixHexString()
        )

        assertEquals(
            "4148811799cb2d2aa038a824a7b9eb958497a7dfaf6cc87de2218f71dc26a7d5",
            mail.hashStruct().toNoPrefixHexString()
        )
    }

    @Test
    fun testSignature() {
        val mail = Struct712(
            typeName = "Mail",
            parameters = listOf(
                "from" asParameterNameFor FROM_PERSON,
                "to" asParameterNameFor TO_PERSON,
                "contents" asParameterNameFor CONTENTS
            )
        )

        val payload = typedDataHash(message = mail, domain = TEST_DOMAIN)

        assertEquals(
            "be609aee343fb3c4b28e1df9e632fca64fcfaede20f02e86244efddf30957bd2",
            payload.toNoPrefixHexString()
        )

        val privateKey = "cow".toByteArray(Charsets.UTF_8).keccak()
        val keyPair = PrivateKey(privateKey).toECKeyPair()
        val address = keyPair.toAddress()

        assertEquals(
            "cd2a3d9f938e13cd947ec05abc7fe734df8dd826",
            address.cleanHex
        )

        val signature = signMessageHash(payload, keyPair, true)

        assertThat(28.toBigInteger()).isEqualTo(signature.v)

        assertEquals(
            HexString("0x4355c47d63924e8a72e509b65029052eb6c299d53a04e167c5775fd466751c9d").hexToBigInteger(),
            signature.r
        )

        assertEquals(
            HexString("0x07299936d304c153f6443dfa05f40ff007d72911b6f72307f996231605b91562").hexToBigInteger(),
            signature.s
        )

    }

}
