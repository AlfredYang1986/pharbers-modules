package com.pharbers.unitTest.newBuilder

import com.pharbers.pactions.actionbase.pActionTrait

abstract class Builder extends pActionTrait{
    def setCompany(company: String): pActionTrait
    def setData(data: String): pActionTrait
    def setMarket(market: String): pActionTrait
    def setCpa_file(cpa_file: String): pActionTrait
    def setResultMatch_file(resultMatch_file: String): pActionTrait
    def build(): pActionTrait
}
