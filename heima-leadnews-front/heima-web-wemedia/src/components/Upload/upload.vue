<template>
  <div>
    <el-upload
      ref="upload"
      class="uploader"
      action
      accept=".jpg,.png"
      :show-file-list="true"
      :auto-upload="false"
      :limit="1"
      :on-change="handleChange"
      :on-remove="handleRemove"
      :on-exceed="handleExceed"
      >
      <svg v-if="!uploadImgUrl" class="icon svg-icon" aria-hidden="true">
        <use xlink:href="#icon_btn_addpic" />
      </svg>
      <span v-if="!uploadImgUrl">选择图片</span>
      <img v-if="uploadImgUrl" :src="uploadImgUrl" class="avatar">
      <div
        slot="tip"
        class="el-upload__tip">
        支持扩展名：jpg、png，文件不得大于2MB
      </div>
    </el-upload>
    <el-button type="success" class="el-button--has-icon" @click="fnUpload" style="margin-top: 30px;">
      <svg class="icon svg-icon" aria-hidden="true">
        <use xlink:href="#iconicon_btn_tjsh" />
      </svg>
      开始上传
    </el-button>
  </div>
</template>

<script>
import { uploadImg } from '@/api/publish'

export default {
  name: 'upload',
  props: ['dialogVisible'],
  data () {
    return {
      uploadImgUrl: '',
      file: {}
    }
  },
  watch: {
    dialogVisible: function (newVal, oldVal) {
      // 关闭对话框前，清空上传文件
      if (!newVal) {
        this.clearFiles()
      }
    }
  },
  methods: {
    handleChange (file, fileList) {
      this.file = file
    },
    handleExceed (files, fileList) {
      this.$set(fileList[0], 'raw', files[0])
      this.$set(fileList[0], 'name', files[0].name)
      this.$refs.upload.clearFiles() // 清除文件
      this.$refs.upload.handleStart(files[0]) // 选择文件后的赋值方法
    },
    handleRemove (file, fileList) {
      this.clearFiles()
    },
    // 上传图片
    async fnUpload () {
      const file = this.file.raw
      if (!file) {
        this.$message.error('请选择图片')
        return
      }

      const isJPG =
      file.type === 'image/jpeg' ||
      file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG) {
        this.$message.error('上传图片只能是jpg/png格式')
        return
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过2MB')
        return
      }

      const fd = new FormData()
      fd.append('multipartFile', file, file.name)

      const result = await uploadImg(fd)
      if (result.code === 0) {
        this.$message({ message: '上传成功', type: 'success' })
        this.uploadImgUrl = result.data.url
        this.$emit('uploadSuccess', this.uploadImgUrl)
        this.clearFiles()
      } else {
        this.$message({ message: result.error_message, type: 'error' })
      }
    },
    clearFiles () {
      // TODO: 最好用form resetFields
      this.$refs.upload.clearFiles()
      this.uploadImgUrl = ''
      this.file = {}
    }
  }
}
</script>

<style lang="scss">
@import '@/scss/element-variables.scss';

// TODO: 线上样式和本地不一致，可能是webpack配置导致
.el-dialog--center .el-dialog__body {
  padding: 40px 49px 19px 49px;
}

.uploader {
  text-align: left;

  .el-upload {
    position: relative;
    width: 225px;
    height: 150px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px dashed $--border-color-base;
    color: $--color-text-primary;

    .icon {
      width: 40px;
      height: 40px;
    }

    .avatar {
      width: 225px;
      height: 150px;
      object-fit: cover;
    }
  }

  .el-upload:hover {
    color: $--color-primary;
  }

  .el-upload-list__item {
    height: 40px;
    line-height: 40px;
  }

  .el-upload-list__item:hover {
    background-color: #F0F0F0;
  }

  .el-upload-list__item-name {
    margin-left: 14px;
    color: $--color-text-secondary;
  }

  .el-upload-list__item:first-child {
    margin-top: 20px;
  }

  .el-upload-list__item .el-icon-close {
    top: 13px;
    right: 13px;
  }
}
</style>
