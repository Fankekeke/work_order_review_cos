<template>
  <a-modal v-model="show" title="新增规则" @cancel="onClose" :width="600">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        提交
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">
      <a-row :gutter="20">
        <a-col :span="12">
          <a-form-item label='规则名称' v-bind="formItemLayout">
            <a-input v-decorator="[
      'ruleName',
      { rules: [{ required: true, message: '请输入规则名称!' }] }
    ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='是否启用' v-bind="formItemLayout">
            <a-select v-decorator="[
      'isActive',
      { rules: [{ required: true, message: '请选择是否启用!' }] }
    ]">
              <a-select-option :value="1">启用</a-select-option>
              <a-select-option :value="0">禁用</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='关联风险关键词(逗号分隔)' v-bind="formItemLayout">
            <a-textarea
              v-decorator="[
        'keyword',
        { rules: [{ required: true, message: '请输入关联风险关键词!' }] }
      ]"
              :rows="3"
              placeholder="请输入关联风险关键词，多个关键词请用逗号分隔"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='必须包含的安全措施关键词' v-bind="formItemLayout">
            <a-textarea
              v-decorator="[
        'mandatoryMeasure',
        { rules: [{ required: true, message: '请输入安全措施关键词!' }] }
      ]"
              :rows="3"
              placeholder="请输入必须包含的安全措施关键词"
            />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='触发规则时的警告提示词' v-bind="formItemLayout">
            <a-textarea
              v-decorator="[
        'riskWarning',
        { rules: [{ required: true, message: '请输入警告提示词!' }] }
      ]"
              :rows="3"
              placeholder="请输入触发规则时的警告提示词"
            />
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
  </a-modal>
</template>

<script>
import {mapState} from 'vuex'
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
const formItemLayout = {
  labelCol: { span: 24 },
  wrapperCol: { span: 24 }
}
export default {
  name: 'BulletinAdd',
  props: {
    bulletinAddVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinAddVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$post('/cos/audit-rule-library', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.ant-form-item-label > label {
  font-weight: bold;
  color: #333;
}
</style>
