package com.pharbers.unitTest.newBuilder
import com.pharbers.pactions.actionbase.{StringArgs, pActionArgs}

class NhwaBuilder(var company: String,
                  var data: String,
                  var market: String,
                  var cpa_file: String,
                  var resultMatch_file: String) extends Builder {
    override val name : scala.Predef.String = "Nhwa"
    override val defaultArgs : com.pharbers.pactions.actionbase.pActionArgs = " ".asInstanceOf[StringArgs]
    
    protected def this() = this("", "", "", "", "")
    
    protected def this(pb: NhwaBuilder) = this(
        pb.company,
        pb.data,
        pb.market,
        pb.cpa_file,
        pb.resultMatch_file
    )
    
    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = ???
    
    override def setMarket(market: String):NhwaBuilder ={
        this.market = market
        new NhwaBuilder(this)
    }
    
    override def setCompany(company: String):NhwaBuilder = {
        this.company = company
        new NhwaBuilder(this)
    }
    
    override def setCpa_file(cpa_file: String): NhwaBuilder = {
        this.cpa_file = cpa_file
        new NhwaBuilder(this)
    }
    
    override def setData(data: String): NhwaBuilder = {
        this.data = data
        new NhwaBuilder(this)
    }
    
    override def setResultMatch_file(resultMatch_file: String): NhwaBuilder = {
        this.resultMatch_file = resultMatch_file
        new NhwaBuilder(this)
    }
    
    override def build(): NhwaBuilder = {
        new NhwaBuilder(this)
    }
    
    override def progressFunc(progress: Double, flag: String): Unit = ???
}

object NhwaBuilder{
    def apply() = new NhwaBuilder()
}
