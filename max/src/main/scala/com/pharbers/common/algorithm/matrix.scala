package com.pharbers.common.algorithm

class matrix {

    val limited = 100
    val x_upper_bound = limited
    val y_upper_bound = limited

    var col : Int = 0
    var row : Int = 0

    val _matrix : Array[Int] = new Array[Int](x_upper_bound * y_upper_bound)

    def resize(x : Int, y : Int) : Unit= {
        if (x > x_upper_bound || y > y_upper_bound) throw new Exception("out of boundary")

        col = x
        row = y
    }

    def vaildUpperBoundary(x : Int, y : Int) : Boolean = x < col && y < row
    def vaildLowerBoundary(x: Int, y : Int) : Boolean = x >= 0 && y >= 0

    def setBrustValue(x : Int, y : Int, value : Int) : Int = {
        if (vaildUpperBoundary(x, y) && vaildLowerBoundary(x, y)) {
            _matrix(x + y * col) = value;
            value
        } else throw new Exception("out of boundary")
    }

    def getBrustValue(x : Int, y : Int) : Int = {
        if (!vaildLowerBoundary(x, y)) limited
        else if (!vaildUpperBoundary(x, y)) limited
        else _matrix(x + y * col)
    }
}
