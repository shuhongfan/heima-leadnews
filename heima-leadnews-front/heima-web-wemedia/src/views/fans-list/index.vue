<template>
  <div>
    <!-- 粉丝概况/粉丝画像/粉丝列表 -->
    <manage-tab-bar />

    <div class="content-card">
      <el-card
        v-for="(item, index) in fansList"
        :key="index"
        :body-style="{ padding: '0px' }"
        shadow="hover"
      >
        <img class="image" :src="item.avatar" />
        <div class="name">{{ item.name }}</div>
        <div class="operate">
          <div class="item">
            <el-button class="follow">关注</el-button>
          </div>
          <div class="item">
            <el-button class="contact">私信</el-button>
          </div>
        </div>
      </el-card>
    </div>
    <el-button class="more">点击查看更多</el-button>
  </div>
</template>

<script>
import ManageTabBar from '../fans-statistics/components/ManageTabBar'

const fans01Png = require('@/assets/fans/fans01.png')
const fans02Png = require('@/assets/fans/fans02.png')
const fans03Png = require('@/assets/fans/fans03.png')
const fans04Png = require('@/assets/fans/fans04.png')
const fans05Png = require('@/assets/fans/fans05.png')
const fans06Png = require('@/assets/fans/fans06.png')
const fans07Png = require('@/assets/fans/fans07.png')
const fans08Png = require('@/assets/fans/fans08.png')
const fans09Png = require('@/assets/fans/fans09.png')
const fans10Png = require('@/assets/fans/fans10.png')
const fans11Png = require('@/assets/fans/fans11.png')
const fans12Png = require('@/assets/fans/fans12.png')
const fans13Png = require('@/assets/fans/fans13.png')
const fans14Png = require('@/assets/fans/fans14.png')
const fans15Png = require('@/assets/fans/fans15.png')

export default {
  name: 'FansList',
  data () {
    return {
      fansList: [
        {
          name: 'Tyler Pearson',
          avatar: fans01Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans02Png
        },
        {
          name: 'Tyler Pearson',
          avatar: fans03Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans04Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans05Png
        },
        {
          name: '覃妹艺',
          avatar: fans06Png
        },
        {
          name: '尼芳',
          avatar: fans07Png
        },
        {
          name: '苑婕',
          avatar: fans08Png
        },
        {
          name: '辛翰力',
          avatar: fans09Png
        },
        {
          name: '空黛枫',
          avatar: fans10Png
        },
        {
          name: '阎澜菁',
          avatar: fans11Png
        },
        {
          name: '扈霄芬',
          avatar: fans12Png
        },
        {
          name: '蓬天宜',
          avatar: fans13Png
        },
        {
          name: '字妍',
          avatar: fans14Png
        },
        {
          name: '寇功宁',
          avatar: fans15Png
        },
        {
          name: 'Tyler Pearson',
          avatar: fans01Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans02Png
        },
        {
          name: 'Tyler Pearson',
          avatar: fans03Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans04Png
        },
        {
          name: 'Ophelia Bridges',
          avatar: fans05Png
        }
      ],
      params: {
        page: 1,
        size: 10
      },
      total: 0
    }
  },
  components: {
    ManageTabBar
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.content-card {
  display: flex;
  flex-wrap: wrap;

  .el-card {
    position: relative;
    width: 186px;
    height: 222px;
    margin-left: 30px;
    margin-bottom: 32px;
    border-radius: 6px;
    border: 1px solid $--background-color-base;
    background: $--color-white;

    .image {
      display: block;
      width: 72px;
      height: 72px;
      margin: 34px auto 0;
    }

    .name {
      margin-top: 19px;
      text-align: center;
      font-weight: 600;
      color: $--color-text-secondary;
    }

    .operate {
      display: flex;
      height: 57px;
      margin-top: 23px;
      justify-content: space-around;
      align-items: center;

      .item {
        display: flex;
        align-items: center;
        font-size: 12px;
        cursor: pointer;
      }

      .follow,
      .contact {
        width: 70px;
        height: 31px;
        padding: 0;
        border: none;
        background: $--background-color-base;
      }
    }
  }

  .el-card.is-always-shadow,
  .el-card.is-hover-shadow:focus,
  .el-card.is-hover-shadow:hover {
    -webkit-box-shadow: 0px 2px 8px 4px rgba(51, 53, 58, 0.07);
    box-shadow: 0px 2px 8px 4px rgba(51, 53, 58, 0.07);
  }
}

.more {
  display: block;
  width: 207px;
  height: 50px;
  margin: 0 auto;
  border-radius: 4px;
  border: none;
  background: #d6dff2;
  color: $--color-primary;
}
</style>
