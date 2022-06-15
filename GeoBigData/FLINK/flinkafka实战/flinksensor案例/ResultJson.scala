package com.bainan.test

import java.util

/**
 *
 * @author Max
 * @date 2021/4/1 12:29
 */
case class ResultJson(machineNumber : String,
                      machineType : String,
                      types : String,
                      info : String,
                      pTime : String,
                      data : util.ArrayList[Element]
                     )
