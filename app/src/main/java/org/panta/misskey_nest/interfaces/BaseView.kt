package org.panta.misskey_nest.interfaces

interface BaseView<T: BasePresenter> {
    var mPresenter: T?
}