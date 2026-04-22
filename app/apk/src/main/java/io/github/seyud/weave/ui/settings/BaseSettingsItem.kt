package io.github.seyud.weave.ui.settings

import android.content.Context
import android.view.View

/**
 * 基础设置项类
 * 保留基础类型定义供其他模块使用
 */
sealed class BaseSettingsItem {

    /**
     * 带值的设置项
     */
    abstract class Value<T> : BaseSettingsItem() {
        /**
         * 当前值
         */
        abstract var value: T
    }

    /**
     * 开关类型设置项
     */
    abstract class Toggle : Value<Boolean>() {
        /**
         * 是否选中
         */
        open val isChecked: Boolean get() = value
    }

    /**
     * 输入类型设置项
     */
    abstract class Input : Value<String>() {
        /**
         * 输入结果
         */
        abstract val inputResult: String?

        /**
         * 获取输入视图
         */
        abstract fun getView(context: Context): View
    }

    /**
     * 选择器类型设置项
     */
    abstract class Selector : Value<Int>() {
        /**
         * 条目资源 ID
         */
        open val entryRes: Int get() = -1

        /**
         * 描述资源 ID
         */
        open val descriptionRes: Int get() = entryRes
    }

    /**
     * 空白类型设置项
     */
    abstract class Blank : BaseSettingsItem()

    /**
     * 分组标题类型设置项
     */
    abstract class Section : BaseSettingsItem()
}
