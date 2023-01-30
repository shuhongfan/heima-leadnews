<template>
  <div class="tinymce-container bgc">
    <el-form ref="form" :model="formData" :rules="rules" hide-required-asterisk size="medium">
      <el-form-item prop="title">
        <el-input
          v-model="formData.title"
          placeholder="请在这里输入标题"
          maxlength="30"
          show-word-limit
          class="filter-item title"
        />
      </el-form-item>
      <el-form-item prop="content">
        <Heima ref="heima" @addImg="selectHeiMaImg" />
      </el-form-item>
      <el-form-item>
        <el-col :span="4">
          <el-form-item label="标签：" prop="labels" label-width="55px">
            <el-input v-model="formData.labels" placeholder="请输入标签" class="filter-item" maxlength="20" show-word-limit />
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="频道：" prop="channelId" label-width="85px">
            <el-select v-model="formData.channelId" placeholder="请选择频道">
              <el-option
                v-for="item in channelList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="定时：" prop="publishTime" label-width="85px">
            <el-date-picker
              v-model="formData.publishTime"
              type="datetime"
              placeholder="请选择日期时间"
              default-time="12:00:00"
              style="width:100%;"
              :picker-options="pickerOptions"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-form-item>
      <el-form-item prop="images">
        <el-row>
          <el-form-item label="封面：" label-width="55px">
            <el-radio-group v-model="formData.type">
              <el-radio label="1">单图</el-radio>
              <el-radio label="3">三图</el-radio>
              <el-radio label="0">无图</el-radio>
              <el-radio label="-1">自动</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item v-if="formData.type == '1' || formData.type == '3'">
            <div v-if="formData.type == '1'" class="single_pic" @click="selectSinglePic">
              <svg v-if="!singlePic" class="icon svg-icon" aria-hidden="true">
                <use xlink:href="#icon_btn_addpic" />
              </svg>
              <span v-if="!singlePic">选择图片</span>
              <img v-if="singlePic" :src="parseImage(singlePic)" />
            </div>
            <div v-if="formData.type == '3'" class="three_pic">
              <div
                class="three_pic_item"
                v-for="(item,index) in threePicList"
                :key="index"
                @click="selectThreePic(index)"
              >
                <svg v-if="!item" class="icon svg-icon" aria-hidden="true">
                  <use xlink:href="#icon_btn_addpic" />
                </svg>
                <span v-if="!item">选择图片</span>
                <img v-if="item" :src="parseImage(item)" />
              </div>
            </div>
          </el-form-item>
          </el-row>
        </el-form-item>
      <el-form-item class="btn">
        <el-button type="warning" class="filter-item el-button--has-icon" @click="publish(true)">
          <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#iconicon_btn_savesketch" />
          </svg>
          存入草稿
        </el-button>
        <el-button type="success" class="filter-item el-button--has-icon" @click="publish(false)">
          <svg class="icon svg-icon" aria-hidden="true">
            <use xlink:href="#iconicon_btn_tjsh" />
          </svg>
          提交审核
        </el-button>
      </el-form-item>
    </el-form>
    <material-select-dialog :showPicDialog.sync="showPicDialog" @btnOKImg="btnOKImg"/>
  </div>
</template>

<script>
import Heima from '@/components/editor/heima.vue'
import MaterialSelectDialog from './components/material-select-dialog'
import { getArticleById } from '@/api/content'
import { getChannels, publishArticles, modifyArticles } from '@/api/publish'
import moment from 'moment'

export default {
  name: 'NewsPublish',
  components: { Heima, MaterialSelectDialog },
  data () {
    const validateTitle = (rule, value, callback) => {
      const draft = this.draft
      if (draft === undefined) {
        return callback()
      }
      if (draft) {
        if (!value) {
          return callback(new Error('文章标题不能为空'))
        }
      } else {
        if (!value || value.length < 4) {
          return callback(new Error('文章标题不能小于4个字符'))
        }
      }
      callback()
    }
    const validateContent = (rule, value, callback) => {
      const content = JSON.parse(this.$refs.heima.getContent())
      if (!content.length) {
        return callback(new Error('文章内容不能为空'))
      }
      callback()
    }
    const validateLabels = (rule, value, callback) => {
      const draft = this.draft
      if (draft === undefined) {
        return callback()
      }
      if (!draft) {
        if (!value) {
          return callback(new Error('内容标签不能为空'))
        }
      }
      callback()
    }
    const validatePublishTime = (rule, value, callback) => {
      const draft = this.draft
      if (draft === undefined) {
        return callback()
      }
      if (!draft) {
        if (!value) {
          return callback(new Error('请选择日期时间'))
        }
      }
      callback()
    }
    const validateImages = (rule, value, callback) => {
      const draft = this.draft
      if (draft === undefined) {
        return callback()
      }
      if (!draft) {
        // 文章布局
        // 0 无图文章
        // 1 单图文章
        // 3 多图文章
        const type = this.formData.type
        if (type === '1' && !this.singlePic) {
          return callback(new Error('文章封面未设置'))
        }
        if (type === '3' && this.threePicList.includes(null)) {
          return callback(new Error('文章封面未设置'))
        }
      }
      callback()
    }

    return {
      host: '',
      newsId: '',
      formData: {
        id: '',
        title: '', // 标题
        type: '1',
        labels: '',
        publishTime: '',
        channelId: null, // 频道ID,
        content: ''
      },
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() < Date.now() - 8.64e7 // - 8.64e7 表示可选择当天时间
        }
      },
      singlePic: null, // 单图模式
      threePicList: [null, null, null], // 三图模式
      channelList: [],
      currentType: {
        key: 0, // 编辑序列
        type: '' // 存储弹层的操作类型  single three insert  之所以用对象是因为要存放三张图的索引
      },
      draft: undefined,
      rules: {
        title: [{ validator: validateTitle, trigger: '' }],
        labels: [{ validator: validateLabels, trigger: '' }],
        content: [{ validator: validateContent, trigger: '' }],
        channelId: { required: true, message: '请选择频道', trigger: '' },
        publishTime: [{ validator: validatePublishTime, trigger: '' }],
        images: [{ validator: validateImages, trigger: '' }]
      },
      showPicDialog: false
    }
  },
  created () {
    const { newsId } = this.$route.query
    this.newsId = newsId
    if (this.newsId) {
      // 如果id存在 则拉取新数据
      this.getArticle(this.newsId)
    }
    this.getChannels() // 拉取文章频道
  },
  methods: {
    // 获取文章频道
    async getChannels () {
      const result = await getChannels()
      this.channelList = result.data
    },
    parseImage: function (item) {
      if (item.indexOf('http') > -1) {
        return item
      } else {
        return this.host + item
      }
    },
    // 获取文章
    async getArticle (id) {
      const result = await getArticleById(id)

      // 草稿自动文章，type后台返回值是null，需要转换成-1，这样封面radio才能回显
      const type = result.data.type === null ? -1 : result.data.type
      this.formData = {
        id: result.data.id,
        title: result.data.title,
        channelId: result.data.channelId,
        labels: result.data.labels,
        type: '' + type,
        publishTime: result.data.publishTime
      }
      let conts = []
      if (result.data.content) {
        try {
          // TODO: 这个地方这样不容易理解，需要重构下
          conts = eval('(' + result.data.content + ')')
        } catch (err) {
          console.log('err: ' + err)
        }
      }
      this.$refs.heima.setContent(conts)
      this.host = result.host
      this.transImages(this.formData.type, result.data.images) // 还原数据
    },
    selectHeiMaImg (key) {
      this.currentType.key = key
      this.currentType.type = 'insert'
      this.showPicDialog = true
    },
    // 点击三图中的图片
    selectThreePic (index) {
      this.currentType.type = 'three'
      this.currentType.index = index // 这里需要记录图片的索引 因为要按照顺序 不能乱
      this.showPicDialog = true
    },
    // 选择单张图片
    selectSinglePic () {
      this.currentType.type = 'single'
      this.showPicDialog = true
    },
    // 转换图片
    transImages (type, images) {
      images = images.split(',')
      if (type === '1') {
        this.singlePic = images[0]
      } else if (type === '3') {
        this.threePicList = [...images]
      }
    },
    // 获取图片列表
    getImages () {
      if (
        this.formData.type === '1' ||
        this.formData.type === '3'
      ) {
        if (this.formData.type === '1') {
          return this.singlePic ? [this.singlePic] : []
        } else {
          return this.threePicList.map(item => item)
        }
      }
      return []
    },
    btnOKImg (selectedImg) {
      if (this.currentType.type === 'single') {
        this.singlePic = selectedImg
        this.validateImagesField()
      } else if (this.currentType.type === 'three') {
        this.threePicList[this.currentType.index] = selectedImg
        this.validateImagesField()
      } else if (this.currentType.type === 'insert') {
        this.$refs.heima.saveImage(this.currentType.key, selectedImg)
      }
    },
    validateImagesField () {
      if (this.draft === false) {
        try {
          this.$refs.form.validateField('images')
        } catch (err) {
          console.log('err: ' + err)
        }
      }
    },
    // 发布文章
    async publish (draft) {
      this.draft = draft
      try {
        await this.$refs.form.validate()

        const params = { draft }
        let publishTime = this.formData.publishTime
        if (publishTime) {
          publishTime = moment(this.formData.publishTime).format('YYYY-MM-DDTHH:mm:ssZ')
        }
        const images = this.getImages()
        // TODO: 这个地方这样不容易理解，需要重构下
        const content = JSON.stringify(JSON.parse(this.$refs.heima.getContent()).filter((item) => {
          return !(item.type === 'text' && item.value === '请在这里输入正文')
        }))
        const data = { ...this.formData }
        data.publishTime = publishTime
        data.images = images
        data.status = draft ? 0 : 1
        data.content = content

        let result
        // 编辑或者发布文章
        if (!this.newsId) {
          result = await publishArticles(params, data)
        } else {
          result = await modifyArticles(this.newsId, params, data)
        }
        if (result.code !== 0) {
          this.$message({ type: 'warning', message: result.errorMessage })
          return
        }
        this.$message({ type: 'success', message: `${!this.newsId ? '新增' : '编辑'}文章成功` })
        this.$router.replace({ path: '/news/index' })
      } catch (err) {
        console.log('err: ' + err)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/scss/element-variables.scss';

.tinymce-container {
  padding: 30px;
  text-align: left;

  .el-form {
    .title {
      /deep/ .el-input__inner {
        padding: 0;
        padding-bottom: 18px;
        border: none;
        border-bottom: 2px solid #ebeef5;
        border-radius: 0;
        height: auto;
        line-height: 26px;
        font-size: 20px;
        color: $--color-text-primary;
      }
    }

    // .el-form-item {
    //   margin: 20px 0;
    // }

    .btn {
      border-top: 2px solid #ebeef5;
      // margin: 0 15px;
      // padding: 30px 0;
      padding-top: 31px;
      margin-bottom: 0;
    }
  }
}

.single_pic {
  width: 278px;
  height: 180px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed $--border-color-base;
  border-radius: 4px;
  cursor: pointer;
}

.single_pic:hover, .three_pic_item:hover {
  color: $--color-primary;
}

.single_pic .icon, .three_pic .three_pic_item .icon {
  width: 40px;
  height: 40px;
}

.single_pic span, .three_pic .three_pic_item span {
  line-height: normal;
}

.single_pic img, .three_pic .three_pic_item img {
  width: 278px;
  height: 180px;
  object-fit: cover;
}

.three_pic {
  display: flex;
  flex-direction: row;
  width: 840px;
  height: 180px;
  cursor: pointer;

  .three_pic_item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 2px dashed $--border-color-base;
    border-right: none;
  }

  .three_pic_item:last-child {
    border-right: 2px dashed $--border-color-base;
  }
}
</style>
