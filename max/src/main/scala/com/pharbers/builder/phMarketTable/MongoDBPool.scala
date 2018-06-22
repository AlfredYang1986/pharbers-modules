package com.pharbers.builder.phMarketTable

import com.pharbers.dbManagerTrait.dbInstanceManager

object MongoDBPool {
    val MongoPool: dbInstanceManager = new dbInstanceManager {}
}
