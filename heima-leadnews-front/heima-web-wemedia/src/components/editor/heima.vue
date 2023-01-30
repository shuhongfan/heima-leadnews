<template>
  <div class="item-wapper">
    <div class="item" v-for="(item, key) in content" :key="key">
      <div class="item-t-bar">
        <li @click="deleteItem(key)" title="删除">&#xf00d;</li>
        <li @click="clearStyle(key)" title="恢复样式" v-if="item.type=='text'">&#xf122;</li>
        <li @click="enditorText(key)" title="编辑内容" v-if="item.type=='text'">&#xf044;</li>
        <li @click="editImg(key)" title="重新选择" v-if="item.type=='image'">&#xf044;</li>
        <li @click="bold(key)" title="加粗" v-if="item.type=='text'">&#xf032;</li>
        <li @click="up(key)" title="上移">&#xf062;</li>
        <li @click="down(key)" title="下移">&#xf063;</li>
        <li @click="upFontSize(key)" title="加大字号" v-if="item.type=='text'">&#xf15d;</li>
        <li @click="downFontSize(key)" title="减小字号" v-if="item.type=='text'">&#xf15e;</li>
        <li @click="addText(key)" title="添加文字" style="float: left">&#xf034;</li>
        <li @click="addImg(key)" title="添加图片" style="float: left">&#xf03e;</li>
      </div>
      <div class="item-t" v-if="item.type=='text'" :style="item.style">{{item.value}}</div>
      <div class="item-i" v-if="item.type=='image'">
        <img style="max-width: 290px" :src="item.value" />
      </div>
    </div>
    <el-dialog
      width="849px"
      center
      :title="controller.editorTitle"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :visible.sync="controller.dialogTextVisible"
    >
      <el-form>
        <el-input type="textarea" :rows="5" placeholder="请输入内容" v-model="controller.editorText"></el-input>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" class="el-button--no-icon" @click="saveText('cancel')">取消</el-button>
        <el-button type="success" class="el-button--no-icon" @click="saveText('ok')">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import myMixin from '@/utils/mixins'

export default {
  name: 'HeimaEditor',
  mixins: [myMixin],
  props: {
    datas: {
      type: Array,
      default: function () {
        return [
          {
            type: 'text',
            value: '请在这里输入正文'
          }
        ]
      }
    }
  },
  data () {
    return {
      text: {
        addText: '添加文字',
        editText: '编辑文字'
      },
      controller: {
        editorKey: 0,
        editorTitle: '',
        editorText: '',
        dialogTextVisible: false
      },
      content: []
    }
  },
  created () {
    this.setContent(this.datas)
  },
  methods: {
    setContent: function (data) {
      if (data.length > 0) {
        this.content = data
      } else {
        this.content = this.datas
      }
    },
    deleteItem: async function (key) {
      if (this.content.length > 1) {
        try {
          await this.showDeleteConfirm('')
          this.content.splice(key, 1)
        } catch (err) {
          console.log('err: ' + err)
        }
      } else {
        this.$message({ type: 'warning', message: '不能全部删除内容，请编辑！' })
      }
    },
    clearStyle: function (key) {
      this.getStyle(key, 'w', '0')
      this.$set(this.content[key], 'style', {})
    },
    // 编辑
    editImg: function (key) {
      this.$emit('addImg', { key: key, type: 'edit' })
    },
    // 增加或者删除
    addImg: function (key) {
      this.$emit('addImg', { key: key, type: 'add' })
    },
    // 添加文字
    addText: function (key) {
      this.controller.editorTitle = this.text.addText
      this.controller.editorKey = key
      this.controller.editorText = ''
      this.controller.dialogTextVisible = true
    },
    // 编辑文本
    enditorText: function (key) {
      this.controller.editorTitle = this.text.editText
      this.controller.editorKey = key
      this.controller.editorText = this.content[key].value
      this.controller.dialogTextVisible = true
    },
    // 保存图片
    saveImage: function (data, image) {
      if (data.type === 'add') {
        const value = {
          type: 'image',
          value: image
        }
        this.content.splice(data.key, 0, value)
      } else {
        this.$set(this.content[data.key], 'type', 'image')
        this.$set(this.content[data.key], 'value', image)
      }
    },
    // 保存编辑和新增的文字
    saveText: function (button) {
      if (button === 'ok') {
        if (this.controller.editorText !== '') {
          if (this.controller.editorTitle === this.text.editText) {
            this.$set(this.content[this.controller.editorKey], 'value', this.controller.editorText)
            this.controller.dialogTextVisible = false
          } else {
            this.content.splice(this.controller.editorKey, 0, { type: 'text', value: this.controller.editorText })
            this.controller.dialogTextVisible = false
          }
        } else {
          alert('文字内容不能为空！')
        }
      } else {
        this.controller.dialogTextVisible = false
      }
    },
    bold: function (key) {
      let temp = this.getStyle(key, 'fontWeight', 'normal')
      if (temp !== 'bold') { temp = 'bold' } else { temp = 'normal' }
      this.$set(this.content[key].style, 'fontWeight', temp)
    },
    up: function (key) {
      const i = key - 1
      if (i >= 0) {
        this.content[i] = this.content.splice(key, 1, this.content[i])[0]
      }
    },
    down: function (key) {
      const i = key + 1
      if (i < this.content.length) {
        this.content[i] = this.content.splice(key, 1, this.content[i])[0]
      }
    },
    upFontSize: function (key) {
      const temp = this.getStyle(key, 'fontSize', '12')
      this.$set(this.content[key].style, 'fontSize', (parseInt(temp) + 1) + 'px')
    },
    downFontSize: function (key) {
      const temp = this.getStyle(key, 'fontSize', '12')
      this.$set(this.content[key].style, 'fontSize', (parseInt(temp) - 1) + 'px')
    },
    // 获取一个样式
    getStyle: function (key, name, defValue) {
      let style = this.content[key].style
      if (style === undefined) {
        style = {}
        this.$set(this.content[key], 'style', style)
      }
      let temp = style[name]
      if (temp === undefined) {
        temp = defValue
        this.$set(this.content[key].style, name, defValue)
      } else {
        temp = temp.toLowerCase()
      }
      return temp.replace(';', '').replace('px', '')
    },
    // 过滤空样式
    getItemStyle: function (style) {
      if (style !== undefined) {
        return style
      }
      return {}
    },
    getContent: function () {
      return JSON.stringify(this.content)
    }
  }
}
</script>

<style scoped>
.item-wapper {
  border-bottom: 2px solid #ebeef5;
  /* width: 310px;
    max-height: 550px; */
  /* width: 924px; */
  height: 456px;
  overflow-x: hidden;
  overflow-y: auto;
  padding: 15px 10px;
  /* border-radius: 10px; */
  /* border-radius: 4px; */
}
.item-wapper::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 10px; /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}
.item-wapper::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  background: #dbdbdb;
}
.item-wapper::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  background: transparent;
}
.item {
  position: relative;
  overflow: hidden;
  text-align: left;
  border: 1px solid #ffffff;
}
.item-t {
  min-height: 30px;
  font-size: 12px;
  line-height: 150%;
  margin: 5px 0px;
}
.item-i {
  margin: 5px 0px;
}
.item-i img {
  padding: 0px;
  margin: 0px;
  display: block;
  border: none;
}
.item:hover {
  border: 1px solid #dbdbdb;
  border-radius: 10px;
}
.item:hover .item-t-bar {
  display: inherit;
}
.item-t-bar {
  display: none;
  position: absolute;
  background-color: red;
  opacity: 0.9;
  width: 100%;
  color: #ffffff;
  z-index: 999;
  overflow: hidden;
}
.item-t-bar li {
  list-style: none;
  float: right;
  line-height: 30px;
  background-color: red;
  font-size: 10px;
  font-family: 'FontAwesome';
  padding: 0px 5px;
  cursor: pointer;
}
</style>
