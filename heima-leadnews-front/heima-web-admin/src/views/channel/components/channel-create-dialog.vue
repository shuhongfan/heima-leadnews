<template>
  <el-dialog title="新增频道" :visible.sync="syncDialogVisible" :close-on-click-modal="false" :close-on-press-escape="false" width="442px" @close="close">
    <el-form ref="form" :model="form" :rules="rules" :inline="true" size="medium" hide-required-asterisk>
      <el-form-item label="频道名称：" prop="name">
        <el-input v-model="form.name" autocomplete="off" placeholder="请输入频道名称" maxlength="10" show-word-limit class="dialog-input"></el-input>
      </el-form-item>
      <el-form-item label="频道描述：" prop="description">
        <el-input v-model="form.description" autocomplete="off" placeholder="请输入频道描述" maxlength="16" show-word-limit class="dialog-input"></el-input>
      </el-form-item>
      <el-form-item label="是否启动：" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio
            v-for="(item, index) in stateList"
            :key="index"
            :label="item.label"
          >{{ item.value }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="排序方式：" prop="ord">
        <!-- TODO: 样式还原 -->
        <el-input-number v-model="form.ord" :min="1" :max="99" placeholder="请输入排序方式" class="dialog-input-number"></el-input-number>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="warning" class="el-button--no-icon" @click="syncDialogVisible = false">取消</el-button>
      <el-button type="success" class="el-button--no-icon" @click="ensureDialog">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveData } from '@/api/channel'

export default {
  name: 'ChannelCreateDialog',
  props: ['dialogVisible'],
  data () {
    return {
      // formLabelWidth: '80px',
      form: {
        name: '',
        description: '',
        status: 0,
        ord: 1
      },
      rules: {
        name: [{ required: true, message: '请输入', trigger: 'change' }],
        description: [{ required: true, message: '请输入', trigger: 'change' }],
        status: [{ required: true, message: '请选择', trigger: 'change' }]
      },
      stateList: [
        { label: 1, value: '启动' },
        { label: 0, value: '禁用' }
      ]
    }
  },
  computed: {
    syncDialogVisible: {
      get () {
        return this.dialogVisible
      },
      set (val) {
        this.$emit('update:dialogVisible', val)
      }
    }
  },
  methods: {
    ensureDialog: async function () {
      try {
        await this.$refs.form.validate()

        const res = await saveData(this.form)
        if (res.code === 0) {
          this.syncDialogVisible = false
          this.$emit('refreshList')
          this.$message({ type: 'success', message: '操作成功！' })
        } else {
          this.$message({ type: 'error', message: res.errorMessage })
        }
      } catch (err) {
        console.log('err: ' + err)
      }
    },
    close () {
      this.$nextTick(() => {
        this.$refs.form.resetFields()
      })
    }
  }

}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';
// TODO: 这里再想想
/deep/ .el-dialog__body {
  padding: 30px;
}

.el-form-item {
  margin-bottom: 20px;
}

/deep/ .el-form-item__label {
  color: $--color-text-primary;
}

/deep/ .el-form-item__content {
  color: $--color-text-primary;

  .el-input .el-input__count .el-input__count-inner {
    background: $--background-color-base;
  }
}
</style>
