<template>
  <div class="logout">
    <img class="user-avatar" :src="headImg" />
    <span class="user-name">{{userName}}</span>
    <span class="split-line">|</span>
    <svg class="icon svg-icon" aria-hidden="true" @click="logout">
      <use xlink:href="#iconbtn_top_quite" />
    </svg>
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'
import { getUser, clearUser } from '@/utils/store'
import emitter from '@/utils/event'
export default {
  name: 'LogoutBar',
  mixins: [myMixin],
  data () {
    return {
      user: {}
    }
  },
  created () {
    this.user = getUser() // 获取用户信息
    emitter.$on('userChange', () => {
      this.user = getUser() // 用户信息发生改变时 触发重新拉取新的内容
    })
  },
  computed: {
    userName () {
      return this.user.name ? this.user.name : '未登录'
    },
    headImg () {
      return this.user.photo ? this.user.photo : require('@/assets/index/img_touxiang@2x.png')
    }
  },
  methods: {
    async logout () {
      try {
        await this.showLogoutConfirm()
        clearUser() // 退出前要清除掉用户的信息
        this.$router.replace({ path: '/login' })
      } catch (err) {
        this.$message({ type: 'info', message: '已取消登录' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.logout {
  position: absolute;
  left: 40px;
  bottom: 62px;
  display: flex;
  align-items: center;
  z-index: 1002;

  .user-avatar {
    width: 36px;
    height: 36px;
  }

  .user-name {
    margin-left: 6px;
    color: $--color-text-secondary;
  }

  .split-line {
    margin-left: 12px;
    color: #b6bbc2;
  }

  .svg-icon {
    width: 20px;
    height: 20px;
    margin-top: 3px;
    margin-left: 12px;
    color: $--color-text-secondary;
    vertical-align: middle;
    cursor: pointer;
  }

  .svg-icon:hover {
    color: $--color-primary;
  }
}
</style>
