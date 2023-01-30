<template>
  <div>
    <!-- 粉丝概况/粉丝画像/粉丝列表 -->
    <manage-tab-bar />

    <!-- 粉丝性别比例/粉丝终端分布 -->
    <sex-distribution />

    <!-- 粉丝年龄分布 -->
    <age-distribution />

    <!-- 你的受众喜欢哪些分类的内容 -->
    <favorite-category />
  </div>
</template>

<script>
import ManageTabBar from '../fans-statistics/components/ManageTabBar'
import SexDistribution from './components/SexDistribution'
import AgeDistribution from './components/AgeDistribution'
import FavoriteCategory from './components/FavoriteCategory'

export default {
  name: 'FansPortrait',
  components: {
    ManageTabBar,
    SexDistribution,
    AgeDistribution,
    FavoriteCategory
  }
}
</script>
