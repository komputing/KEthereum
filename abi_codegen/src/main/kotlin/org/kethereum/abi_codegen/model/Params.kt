package org.kethereum.abi_codegen.model

import org.kethereum.contract.abi.types.model.ContractABITypeDefinition

data class Params(val parameterName: String,
                  val typeDefinition: ContractABITypeDefinition?,
                  val type: String)