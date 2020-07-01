package org.kethereum.metadata.model

sealed class UserDocResult

object UserDocResultContractNotFound : UserDocResult()
data class ResolvedUserDocResult(val userDoc: String) : UserDocResult()
data class ResolveErrorUserDocResult(val error: String) : UserDocResult()
object NoMatchingUserDocFound : UserDocResult()