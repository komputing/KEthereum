package org.kethereum.bip32

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.kethereum.bip32.model.Seed
import org.kethereum.bip32.model.XPriv
import org.komputing.khex.extensions.hexToByteArray
import org.komputing.khex.model.HexString

/**
 * Test batch for key derivation from seed given a path.
 *
 * The test vectors used are the ones described in [bip32](https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki)
 */
class DerivationTest {

    @Test
    fun createdKeyHasKeypair() {
        val simpleKey = Seed(ByteArray(0)).toKey("m/0")
        assertThat(simpleKey.keyPair).isNotNull
    }

    @Test
    fun generateKeysFromSeed() {
        testData.forEach {
            val derivedKey = Seed(HexString(it.seed).hexToByteArray()).toKey(it.path)
            val testnetDerivedKey = Seed(HexString(it.seed).hexToByteArray()).toKey(it.path, testnet = true)

            val obtainedPub = derivedKey.serialize(true)
            assertThat(it.expectedPublicKey).isEqualTo(obtainedPub)

            val obtainedTestnetPub = testnetDerivedKey.serialize(true)
            assertThat(it.expectedTestnetPublicKey).isEqualTo(obtainedTestnetPub)

            val obtainedPrv = derivedKey.serialize()
            assertThat(it.expectedPrivateKey).isEqualTo(obtainedPrv)

            val obtainedTestnetPrv = testnetDerivedKey.serialize()
            assertThat(it.expectedTestnetPrivateKey).isEqualTo(obtainedTestnetPrv)

            val parsedPrv = XPriv(it.expectedPrivateKey).toExtendedKey()
            assertThat(derivedKey).isEqualTo(parsedPrv)

            val parsedTestnetPrv = XPriv(it.expectedTestnetPrivateKey).toExtendedKey()
            assertThat(testnetDerivedKey).isEqualTo(parsedTestnetPrv)
        }
    }

    data class DerivationVector(
            val seed: String,
            val path: String,
            val expectedPublicKey: String,
            val expectedPrivateKey: String,
            val expectedTestnetPublicKey: String,
            val expectedTestnetPrivateKey: String
    )

    private val testData = arrayOf(
            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m",
                    "xpub661MyMwAqRbcFtXgS5sYJABqqG9YLmC4Q1Rdap9gSE8NqtwybGhePY2gZ29ESFjqJoCu1Rupje8YtGqsefD265TMg7usUDFdp6W1EGMcet8",
                    "xprv9s21ZrQH143K3QTDL4LXw2F7HEK3wJUD2nW2nRk4stbPy6cq3jPPqjiChkVvvNKmPGJxWUtg6LnF5kejMRNNU3TGtRBeJgk33yuGBxrMPHi",
                    "tpubD6NzVbkrYhZ4XgiXtGrdW5XDAPFCL9h7we1vwNCpn8tGbBcgfVYjXyhWo4E1xkh56hjod1RhGjxbaTLV3X4FyWuejifB9jusQ46QzG87VKp",
                    "tprv8ZgxMBicQKsPeDgjzdC36fs6bMjGApWDNLR9erAXMs5skhMv36j9MV5ecvfavji5khqjWaWSFhN3YcCUUdiKH6isR4Pwy3U5y5egddBr16m"
            ),

            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m/0'",
                    "xpub68Gmy5EdvgibQVfPdqkBBCHxA5htiqg55crXYuXoQRKfDBFA1WEjWgP6LHhwBZeNK1VTsfTFUHCdrfp1bgwQ9xv5ski8PX9rL2dZXvgGDnw",
                    "xprv9uHRZZhk6KAJC1avXpDAp4MDc3sQKNxDiPvvkX8Br5ngLNv1TxvUxt4cV1rGL5hj6KCesnDYUhd7oWgT11eZG7XnxHrnYeSvkzY7d2bhkJ7",
                    "tpubD8eQVK4Kdxg3gHrF62jGP7dKVCoYiEB8dFSpuTawkL5YxTus5j5pf83vaKnii4bc6v2NVEy81P2gYrJczYne3QNNwMTS53p5uzDyHvnw2jm",
                    "tprv8bxNLu25VazNnppTCP4fyhyCvBHcYtzE3wr3cwYeL4HA7yf6TLGEUdS4QC1vLT63TkjRssqJe4CvGNEC8DzW5AoPUw56D1Ayg6HY4oy8QZ9"
            ),

            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m/0'/1",
                    "xpub6ASuArnXKPbfEwhqN6e3mwBcDTgzisQN1wXN9BJcM47sSikHjJf3UFHKkNAWbWMiGj7Wf5uMash7SyYq527Hqck2AxYysAA7xmALppuCkwQ",
                    "xprv9wTYmMFdV23N2TdNG573QoEsfRrWKQgWeibmLntzniatZvR9BmLnvSxqu53Kw1UmYPxLgboyZQaXwTCg8MSY3H2EU4pWcQDnRnrVA1xe8fs",
                    "tpubDApXh6cD2fZ7WjtgpHd8yrWyYaneiFuRZa7fVjMkgxsmC1QzoXW8cgx9zQFJ81Jx4deRGfRE7yXA9A3STsxXj4CKEZJHYgpMYikkas9DBTP",
                    "tprv8e8VYgZxtHsSdGrtvdxYaSrryZGiYviWzGWtDDKTGh5NMXAEB8gYSCLHpFCywNs5uqV7ghRjimALQJkRFZnUrLHpzi2pGkwqLtbubgWuQ8q"
            ),

            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m/0'/1/2'",
                    "xpub6D4BDPcP2GT577Vvch3R8wDkScZWzQzMMUm3PWbmWvVJrZwQY4VUNgqFJPMM3No2dFDFGTsxxpG5uJh7n7epu4trkrX7x7DogT5Uv6fcLW5",
                    "xprv9z4pot5VBttmtdRTWfWQmoH1taj2axGVzFqSb8C9xaxKymcFzXBDptWmT7FwuEzG3ryjH4ktypQSAewRiNMjANTtpgP4mLTj34bhnZX7UiM",
                    "tpubDDRojdS4jYQXNugn4t2WLrZ7mjfAyoVQu7MLk4eurqFCbrc7cHLZX8W5YRS8ZskGR9k9t3PqVv68bVBjAyW4nWM9pTGRddt3GQftg6MVQsm",
                    "tprv8gjmbDPpbAirVSezBEMuwSu1Ci9EpUJWKokZTYccSZSomNMLytWyLdtDNHRbucNaRJWWHANf9AzEdWVAqahfyRjVMKbNRhBmxAM8EJr7R15"
            ),

            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m/0'/1/2'/2",
                    "xpub6FHa3pjLCk84BayeJxFW2SP4XRrFd1JYnxeLeU8EqN3vDfZmbqBqaGJAyiLjTAwm6ZLRQUMv1ZACTj37sR62cfN7fe5JnJ7dh8zL4fiyLHV",
                    "xprvA2JDeKCSNNZky6uBCviVfJSKyQ1mDYahRjijr5idH2WwLsEd4Hsb2Tyh8RfQMuPh7f7RtyzTtdrbdqqsunu5Mm3wDvUAKRHSC34sJ7in334",
                    "tpubDFfCa4Z1v25WTPAVm9EbEMiRrYwucPocLbEe12BPBGooxxEUg42vihy1DkRWyftztTsL23snYezF9uXjGGwGW6pQjEpcTpmsH6ajpf4CVPn",
                    "tprv8iyAReWmmePqZv8hsVZzpx4KHXRyT4chmHdriW95m11R8Tyi3fDLYDM93bq4NGn1V6eCu5cE3zSQ6hPd31F2ApKXkZgTyn1V78pHjkq1V2v"
            ),

            DerivationVector(
                    "000102030405060708090a0b0c0d0e0f",
                    "m/0'/1/2'/2/1000000000",
                    "xpub6H1LXWLaKsWFhvm6RVpEL9P4KfRZSW7abD2ttkWP3SSQvnyA8FSVqNTEcYFgJS2UaFcxupHiYkro49S8yGasTvXEYBVPamhGW6cFJodrTHy",
                    "xprvA41z7zogVVwxVSgdKUHDy1SKmdb533PjDz7J6N6mV6uS3ze1ai8FHa8kmHScGpWmj4WggLyQjgPie1rFSruoUihUZREPSL39UNdE3BBDu76",
                    "tpubDHNy3kAG39ThyiwwsgoKY4iRenXDRtce8qdCFJZXPMCJg5dsCUHayp84raLTpvyiNA9sXPob5rgqkKvkN8S7MMyXbnEhGJMW64Cf4vFAoaF",
                    "tprv8kgvuL81tmn36Fv9z38j8f4K5m1HGZRjZY2QxnXDy5PuqbP6a5TzoKWCgTcGHBu66W3TgSbAu2yX6sPza5FkHmy564Sh6gmCPUNeUt4yj2x"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m",
                    "xpub661MyMwAqRbcFW31YEwpkMuc5THy2PSt5bDMsktWQcFF8syAmRUapSCGu8ED9W6oDMSgv6Zz8idoc4a6mr8BDzTJY47LJhkJ8UB7WEGuduB",
                    "xprv9s21ZrQH143K31xYSDQpPDxsXRTUcvj2iNHm5NUtrGiGG5e2DtALGdso3pGz6ssrdK4PFmM8NSpSBHNqPqm55Qn3LqFtT2emdEXVYsCzC2U",
                    "tpubD6NzVbkrYhZ4XJDrzRvuxHEyQaPd1mwwdDofEJwekX18tAdsqeKfxss79AJzg1431FybXg5rfpTrJF4iAhyR7RubberdzEQXiRmXGADH2eA",
                    "tprv8ZgxMBicQKsPdqC56nGKYsarqYsgrSm33vCswnuMLFCk3gP7DFW5nPFExzSe7FGAzkbAFrxtXoQEe8vaX471tU3dsUUC7PNpYLGuzb2agmj"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m/0",
                    "xpub69H7F5d8KSRgmmdJg2KhpAK8SR3DjMwAdkxj3ZuxV27CprR9LgpeyGmXUbC6wb7ERfvrnKZjXoUmmDznezpbZb7ap6r1D3tgFxHmwMkQTPH",
                    "xprv9vHkqa6EV4sPZHYqZznhT2NPtPCjKuDKGY38FBWLvgaDx45zo9WQRUT3dKYnjwih2yJD9mkrocEZXo1ex8G81dwSM1fwqWpWkeS3v86pgKt",
                    "tpubD9ejmKSp2iP93ZpA8DJo25eVmY8sikSEBPZ2Q7y6pvs6a95rQufk7iSMidGtU64UDaTmPu5c4uJpTQVQ3rfqT2ZsshbJtaYuqutBhMEvKgw",
                    "tprv8cxhcuQZtLhUA6nNEZeCcfzPCWcwZRFKc5xF7bvoQf4hjeq5nWr9wDpVYViSkK71QQpz9sNcxxpMzeZQ5Lc4phD2setFVsYZfkBUMsgR3x8"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m/0/2147483647'",
                    "xpub6ASAVgeehLbnwdqV6UKMHVzgqAG8Gr6riv3Fxxpj8ksbH9ebxaEyBLZ85ySDhKiLDBrQSARLq1uNRts8RuJiHjaDMBU4Zn9h8LZNnBC5y4a",
                    "xprv9wSp6B7kry3Vj9m1zSnLvN3xH8RdsPP1Mh7fAaR7aRLcQMKTR2vidYEeEg2mUCTAwCd6vnxVrcjfy2kRgVsFawNzmjuHc2YmYRmagcEPdU9",
                    "tpubDAoo1vULQcZFDS2LYfJSVRL4AHMnGEbvGYdZKWssUfdV2SKK2o64KnDxL1X1Dpfa16PK3jwDN7jR85Mjpm9xBB2WQnDNFJoviJ9nYAGqm3T",
                    "tprv8e7ksWS6GEsaKxzYf1dr61fwbFqr6uR1hF2n2zqa4Pq6Bx4YQQGU9Hc69rCRUZqVJe9svtaG1yKURtJAoiDCPzebJP7bGPGpTXX18KaUzow"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m/0/2147483647'/1",
                    "xpub6DF8uhdarytz3FWdA8TvFSvvAh8dP3283MY7p2V4SeE2wyWmG5mg5EwVvmdMVCQcoNJxGoWaU9DCWh89LojfZ537wTfunKau47EL2dhHKon",
                    "xprv9zFnWC6h2cLgpmSA46vutJzBcfJ8yaJGg8cX1e5StJh45BBciYTRXSd25UEPVuesF9yog62tGAQtHjXajPPdbRCHuWS6T8XA2ECKADdw4Ef",
                    "tpubDDcmRwTGaFrSK3hUcKT1TNGHVpEHNRXBaz8RAaYCnYyvhGBULJcmDgcLAoi91hMrbGqrtP2T1F3FCsckjfauSWVR14RDTrF8e4pjnhENZ4d",
                    "tprv8gvjHXR2RtAmRafgifnR3xcAvniMD6LH1gXdt4VuNHBXrmvhhuoB3BzTzeQ3WH3BcbWagBeeRWzgkb5KrbjaQUTtS9eQ7VFCwKwjbvD1VbH"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m/0/2147483647'/1/2147483646'",
                    "xpub6ERApfZwUNrhLCkDtcHTcxd75RbzS1ed54G1LkBUHQVHQKqhMkhgbmJbZRkrgZw4koxb5JaHWkY4ALHY2grBGRjaDMzQLcgJvLJuZZvRcEL",
                    "xprvA1RpRA33e1JQ7ifknakTFpgNXPmW2YvmhqLQYMmrj4xJXXWYpDPS3xz7iAxn8L39njGVyuoseXzU6rcxFLJ8HFsTjSyQbLYnMpCqE2VbFWc",
                    "tpubDEnoLuPdBep9bzw5LoGYpsxUQYheRQ9gcgrJhJEcdKFB9cWQRyYmkCyRoTqeD4tJYiVVgt6A3rN6rWn9RYhR9sBsGxji29LYWHuKKbdb1ev",
                    "tprv8i6mCVMP3H8UiXuHT9bxRUJMqXBiG4xn3PFXQnCKD3SnK8FdoajBZiMZdM8S8hRUAAoGz1RdotaGZiAhNYe56K94G6BiFhGqGuxFfgKQPiw"
            ),

            DerivationVector(
                    "fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542",
                    "m/0/2147483647'/1/2147483646'/2",
                    "xpub6FnCn6nSzZAw5Tw7cgR9bi15UV96gLZhjDstkXXxvCLsUXBGXPdSnLFbdpq8p9HmGsApME5hQTZ3emM2rnY5agb9rXpVGyy3bdW6EEgAtqt",
                    "xprvA2nrNbFZABcdryreWet9Ea4LvTJcGsqrMzxHx98MMrotbir7yrKCEXw7nadnHM8Dq38EGfSh6dqA9QWTyefMLEcBYJUuekgW4BYPJcr9E7j",
                    "tpubDG9qJLc8hq8PMG7y4sQEodLSocEkfj4mGrUC75b7G76mDoqybcUXvmvRsruvLeF14mhixobZwZP6LwqeFePKU83Sv8ZnxWdHBb6VzE6zbvC",
                    "tprv8jTo9vZtZTSiTo6BBDjeQDgLEaipWPsrhYsQpZYoqqJNPKbCyDewkHJZhkoSHiWYCUf1Gm4TFzQxcG4D6s1J9Hsn4whDK7QYyHHokJeUuac"
            ),

            DerivationVector(
                    "4b381541583be4423346c643850da4b320e46a87ae3d2a4e6da11eba819cd4acba45d239319ac14f863b8d5ab5a0d0c64d2e8a1e7d1457df2e5a3c51c73235be",
                    "m",
                    "xpub661MyMwAqRbcEZVB4dScxMAdx6d4nFc9nvyvH3v4gJL378CSRZiYmhRoP7mBy6gSPSCYk6SzXPTf3ND1cZAceL7SfJ1Z3GC8vBgp2epUt13",
                    "xprv9s21ZrQH143K25QhxbucbDDuQ4naNntJRi4KUfWT7xo4EKsHt2QJDu7KXp1A3u7Bi1j8ph3EGsZ9Xvz9dGuVrtHHs7pXeTzjuxBrCmmhgC6",
                    "tpubD6NzVbkrYhZ4WMg2WpRiAGW1HDiime7DLZaDdbyD2D5vrQs9VnZdv96dd9qyVbdgBLjTMfxs4VHhjYhd1R1rXmZjitkrinrNW9HDndbLQPW",
                    "tprv8ZgxMBicQKsPcteEdAm7krqtiCCncJvJmFySM5vubwHY1vcNsPk3jeUmSzAp4GVW5TFupnezSE8wznXtkVFSfwYtPm2qJpinq3wGeUFH2nU"
            ),

            DerivationVector(
                    "4b381541583be4423346c643850da4b320e46a87ae3d2a4e6da11eba819cd4acba45d239319ac14f863b8d5ab5a0d0c64d2e8a1e7d1457df2e5a3c51c73235be",
                    "m/0'",
                    "xpub68NZiKmJWnxxS6aaHmn81bvJeTESw724CRDs6HbuccFQN9Ku14VQrADWgqbhhTHBaohPX4CjNLf9fq9MYo6oDaPPLPxSb7gwQN3ih19Zm4Y",
                    "xprv9uPDJpEQgRQfDcW7BkF7eTya6RPxXeJCqCJGHuCJ4GiRVLzkTXBAJMu2qaMWPrS7AANYqdq6vcBcBUdJCVVFceUvJFjaPdGZ2y9WACViL4L",
                    "tpubD8kCEZazE4vQhtmRjxmDDXFfyaL6vVX7k3pASqf3xX1J7Rzc5HLVzbtLvsgVDxERNiEJ8dibuSVCN1dxwex371qgPzhkGeMAzKe8T7ivSof",
                    "tprv8c4A69Yk5hEjpRjdrK6cp7bZQYpAmALDAkDPAKckYFCuGwjqStWup7GUkkXAQDpRXbuKqjSs5xmQeLB3KhqCRhkWptwt3yzbx4tvbt53nTu"
            )
    )

}