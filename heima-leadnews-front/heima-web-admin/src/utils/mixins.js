export default {
  methods: {
    // 驳回弹窗
    async showPrompt () {
      return await this.$prompt('请输入驳回理由', '驳回理由', {
        confirmButtonText: '提交',
        confirmButtonClass: 'el-button--success el-button--no-icon',
        cancelButtonText: '取消',
        cancelButtonClass: 'el-button--warning el-button--no-icon',
        closeOnClickModal: false,
        closeOnPressEscape: false,
        inputPlaceholder: '请输入驳回理由（50字以内）',
        inputType: 'textarea',
        inputValidator: (value) => {
          if (!value) {
            return '驳回理由不能为空'
          }
          if (value.length > 50) {
            return '驳回理由不能超过50个字符'
          }
          return true
        }
        // inputPattern: /\S/,
        // inputErrorMessage: '驳回理由不能为空'
      })
    },
    // 返回上级
    goBackPage () {
      this.$router.go(-1)
    },
    // TODO: confirmButton默认是聚焦状态
    async showDeleteConfirm (msg) {
      return await this.$confirm(`删除后${msg}将不可找回，确认将${msg}删除吗？`, '删除', {
        confirmButtonText: '删除',
        confirmButtonClass: 'el-button--danger el-button--no-icon',
        cancelButtonText: '取消',
        cancelButtonClass: 'el-button--warning el-button--no-icon',
        closeOnClickModal: false,
        closeOnPressEscape: false
      })
    },
    // TODO: confirmButton默认是聚焦状态
    async showLogoutConfirm (msg) {
      return await this.$confirm('确认要退出登录吗？', '温馨提示', {
        confirmButtonText: '确定',
        confirmButtonClass: 'el-button--danger el-button--no-icon',
        cancelButtonText: '取消',
        cancelButtonClass: 'el-button--warning el-button--no-icon',
        closeOnClickModal: false,
        closeOnPressEscape: false
      })
    }
  }
}
