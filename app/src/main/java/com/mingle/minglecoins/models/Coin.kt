package com.mingle.minglecoins.models

class Coin(var Id: String,
           var Name: String,
           var Url: String?,
           var ImageUrl: String?,
           var CoinName: String?,
           var FullName: String?,
           var Algorithm: String?,
           var ProofType: String?,
           var SortOrder: String?) {


    constructor(id : String, name: String) : this(id, name, null, null, null,
            null, null, null, null)

}


