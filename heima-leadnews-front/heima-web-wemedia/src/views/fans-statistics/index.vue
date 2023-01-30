<template>
  <div>
    <!-- 粉丝概况/粉丝画像/粉丝列表 -->
    <manage-tab-bar />

    <!-- 粉丝统计 -->
    <dimension-statist />

    <!-- 数据列表 -->
    <fans-pagination />
  </div>
</template>

<script>
import ManageTabBar from '../fans-statistics/components/ManageTabBar'
import DimensionStatist from './components/DimensionStatist'
import FansPagination from './components/FansPagination'

export default {
  name: 'FansIndex',
  components: {
    ManageTabBar,
    DimensionStatist,
    FansPagination
  }
}
</script>
