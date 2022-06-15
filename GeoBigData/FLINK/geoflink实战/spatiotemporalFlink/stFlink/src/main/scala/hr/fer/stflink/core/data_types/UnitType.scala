package hr.fer.stflink.core.data_types

class UnitType(firstX: Double, secondX: Double, firstY: Double, secondY: Double, p: TimeInterval) {
    private var _x0: Double = firstX
    private var _y0: Double = firstY
    private var _x1: Double = secondX
    private var _y1: Double = secondY

    private var _tInterval: TimeInterval = p

    def x0 = _x0
    def y0 = _y0

    def x1 = _x1
    def y1 = _y1

    def period = _tInterval

    def x0_= (value: Double):Unit = _x0 = value
    def y0_= (value: Double):Unit = _y0 = value

    def x1_= (value: Double):Unit = _x1 = value
    def y1_= (value: Double):Unit = _y1 = value

    def period_= (value: TimeInterval):Unit = _tInterval = value

}