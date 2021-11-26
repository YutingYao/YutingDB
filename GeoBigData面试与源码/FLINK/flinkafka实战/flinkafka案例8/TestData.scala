package com.machinedoll.experiment.data

import java.time.Instant

case class TestData(string: String,
                    int: Long,
                    bigDecimal: BigDecimal,
                    instant: Instant,
                    nested: TestDataNested,
                    option: Option[String],
                    list: List[String],
                    map: Map[String, TestDataNested]) extends Serializable {
}

