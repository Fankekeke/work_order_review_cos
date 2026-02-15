<template>
  <a-modal v-model="show" title="修改模板流程" @cancel="onClose" :width="800" :footer="null">
    <template slot="footer">
      <a-button key="back" @click="onClose">
        取消
      </a-button>
      <a-button key="submit" type="primary" :loading="loading" @click="handleSubmit">
        修改
      </a-button>
    </template>
    <a-form :form="form" layout="vertical">

      <a-row :gutter="20">
        <a-col :span="24">
          <a-form-item label='模板名称' v-bind="formItemLayout">
            <a-input v-decorator="[
        'workName',
        { rules: [{ required: true, message: '请输入模板名称!' }] }
      ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='作业类型' v-bind="formItemLayout">
            <a-radio-group v-decorator="[
    'workType',
    { rules: [{ required: true, message: '请选择作业类型!' }] }
  ]" button-style="solid">
              <a-radio-button value="动火作业">动火</a-radio-button>
              <a-radio-button value="受限空间作业">受限空间</a-radio-button>
              <a-radio-button value="高处作业">高处</a-radio-button>
              <a-radio-button value="吊装作业">吊装</a-radio-button>
              <a-radio-button value="临时用电作业">临时用电</a-radio-button>
              <a-radio-button value="动土作业">动土</a-radio-button>
              <a-radio-button value="断路作业">断路</a-radio-button>
              <a-radio-button value="盲板抽堵作业">盲板抽堵</a-radio-button>
            </a-radio-group>
          </a-form-item>
        </a-col>

        <a-col :span="24">

          <a-steps :current="currentStep" style="margin-bottom: 20px;margin-top: 20px">
            <a-step title="初审" />
            <a-step title="复审" />
            <a-step title="终审" />
          </a-steps>

          <a-form :form="form" layout="vertical" style="margin-top: 45px">
            <!-- 初审 -->
            <div v-show="currentStep === 0">
              <a-form-item label='初审人员' v-bind="formItemLayout">
                <a-select
                  v-decorator="[
        'reviewer1',
        { rules: [{ required: true, message: '请选择初审人员!' }] }
      ]"
                  placeholder="请选择初审人员"
                  option-label-prop="label"
                >
                  <a-select-option
                    v-for="staff in staffList"
                    :key="staff.id"
                    :value="staff.id"
                    :label="staff.name"
                  >
                    <div style="display: flex; align-items: center;">
                      <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + staff.images" :size="30" />
                      <div style="margin-left: 10px;">
                        <div>{{ staff.name }}</div>
                        <div style="font-size: 12px; color: #999;">{{ staff.deptName }} - {{ staff.positionName }}</div>
                      </div>
                    </div>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </div>

            <!-- 复审 -->
            <div v-show="currentStep === 1">
              <a-form-item label='复审人员' v-bind="formItemLayout">
                <a-select
                  v-decorator="[
        'reviewer2',
        { rules: [{ required: true, message: '请选择复审人员!' }] }
      ]"
                  placeholder="请选择复审人员"
                  option-label-prop="label"
                >
                  <a-select-option
                    v-for="staff in staffList"
                    :key="staff.id"
                    :value="staff.id"
                    :label="staff.name"
                  >
                    <div style="display: flex; align-items: center;">
                      <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + staff.images" size="small" />
                      <div style="margin-left: 10px;">
                        <div>{{ staff.name }}</div>
                        <div style="font-size: 12px; color: #999;">{{ staff.deptName }} - {{ staff.positionName }}</div>
                      </div>
                    </div>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </div>

            <!-- 终审 -->
            <div v-show="currentStep === 2">
              <a-form-item label='终审人员' v-bind="formItemLayout">
                <a-select
                  v-decorator="[
        'reviewer3',
        { rules: [{ required: true, message: '请选择终审人员!' }] }
      ]"
                  placeholder="请选择终审人员"
                  option-label-prop="label"
                >
                  <a-select-option
                    v-for="staff in staffList"
                    :key="staff.id"
                    :value="staff.id"
                    :label="staff.name"
                  >
                    <div style="display: flex; align-items: center;">
                      <a-avatar :src="'http://127.0.0.1:9527/imagesWeb/' + staff.images" size="small" />
                      <div style="margin-left: 10px;">
                        <div>{{ staff.name }}</div>
                        <div style="font-size: 12px; color: #999;">{{ staff.deptName }} - {{ staff.positionName }}</div>
                      </div>
                    </div>
                  </a-select-option>
                </a-select>
              </a-form-item>
            </div>
          </a-form>

          <!-- 步骤控制按钮 -->
          <div style="text-align: center; margin-top: 50px;">
            <a-button v-if="currentStep > 0" @click="prevStep">上一步</a-button>
            <a-button v-if="currentStep < 2" type="primary" @click="nextStep">下一步</a-button>
            <a-button v-if="currentStep === 2" type="primary" @click="handleSubmit">提交</a-button>
          </div>
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
  name: 'BulletinEdit',
  props: {
    bulletinEditVisiable: {
      default: false
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    show: {
      get: function () {
        return this.bulletinEditVisiable
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      currentStep: 0,
      rowId: null,
      formItemLayout,
      form: this.$form.createForm(this),
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  mounted() {
    this.queryStaffList()
  },
  methods: {
    queryStaffList () {
      this.$get('/cos/staff-info/queryList').then((r) => {
        this.staffList = r.data.data
      })
    },
    nextStep() {
      this.currentStep++;
    },
    prevStep() {
      this.currentStep--;
    },
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
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    setFormValues ({...bulletin}) {
      this.rowId = bulletin.id
      let fields = ['workName', 'workType', 'reviewer1', 'reviewer2', 'reviewer3']
      let obj = {}
      Object.keys(bulletin).forEach((key) => {
        if (key === 'images') {
          this.fileList = []
          this.imagesInit(bulletin['images'])
        }
        if (key === 'type' && bulletin[key] != null) {
          bulletin[key] = bulletin[key].toString()
        }
        if (fields.indexOf(key) !== -1) {
          this.form.getFieldDecorator(key)
          obj[key] = bulletin[key]
        }
      })
      this.form.setFieldsValue(obj)
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
        if (image.response !== undefined) {
          images.push(image.response)
        } else {
          images.push(image.name)
        }
      })
      this.form.validateFields((err, values) => {
        values.id = this.rowId
        values.images = images.length > 0 ? images.join(',') : null
        if (!err) {
          this.loading = true
          this.$put('/cos/wf-process-config', {
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

</style>
