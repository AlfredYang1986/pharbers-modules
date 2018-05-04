package com.pharbers.common.algorithm

object ph_alg {

    def min_three(a : Int, b : Int, c : Int) : Int = Math.min(Math.min(a, b), c)

    /************************************************************************/
    /* The Edit Distance for the two string                                 */
    /* if the length of str1 is n and the length of str2 is m               */
    /* the time and space complexity both are n * m                         */
    /************************************************************************/
    def edit_distance(str1 : String, str2 : String) : Int = {
        val m = new matrix

        val col= str1.length
        val row = str2.length

        m.resize(col, row)

        for (row_index <- 0 until row) {
            val c1 : Char = str2(row_index)
            for (col_index <- 0 until col) {
                val c2 : Char = str1(col_index)

                var tmp = 0
                if (c1 == c2) {
                    if (m.vaildLowerBoundary(col_index - 1, row_index - 1)) {
                        tmp = m.setBrustValue(col_index, row_index,
                        m.getBrustValue(col_index - 1, row_index - 1) % m.limited);
                    } else {
                        tmp = m.setBrustValue(col_index, row_index,
                        Math.min(m.getBrustValue(col_index - 1, row_index)
                            , m.getBrustValue(col_index, row_index - 1)) % m.limited);
                    }
                } else {
                    // 15 * 15 matrix shall not have two string edit_distanc greater than 15;
                    //int left = m.getBrustValue(col_index - 1, row_index);
                    //int up = m.getBrustValue(col_index, row_index - 1);
                    //int left_up = m.getBrustValue(col_index - 1, row_index - 1);
                    tmp = min_three(m.getBrustValue(col_index - 1, row_index),
                                    m.getBrustValue(col_index, row_index - 1),
                                    m.getBrustValue(col_index - 1, row_index - 1)) % m.limited + 1
                    m.setBrustValue(col_index, row_index, tmp)
                }
            }
        }

        m.getBrustValue(col - 1, row - 1)
    }

    /************************************************************************/
    /* The Hamming_Distance for the two string                              */
    /* if the length of str1 is n and the length of str2 is m               */
    /* the time complexity is min(n, m)                                     */
    /************************************************************************/
    def hamming_distance(str1 : String, str2 : String) : Int = {
        val count = Math.min(str1.length, str2.length)
        val max_len = Math.max(str1.length, str2.length)

        var reVal = max_len - count
        for (index <- 0 until count) {
            if (str1(index) != str2(index))
                reVal += 1
        }

        reVal
    }

    /************************************************************************/
    /* The alphabet order for string str1 and string str2                   */
    /* if str1 < str2, return -1                                            */
    /* if str1 > str2, return 1                                             */
    /* if str1 == str2, return 0                                            */
    /************************************************************************/
    def str_compare(str1 : String, str2 : String) : Int = str1.compareTo(str2)
}
