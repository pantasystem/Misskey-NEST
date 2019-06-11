package org.panta.misskeynest.interfaces

interface BaseView<T: BasePresenter> {
    var mPresenter: T?
}