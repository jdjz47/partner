<script setup>
import {ref} from "vue";
import myAxios from "../../plusing/myAxios.js";
import {showToast} from "vant";
import {useRoute} from "vue-router";

const route=useRoute();
const id=route.query.id;
console.log(id,"id")
const formData=ref({
  name:'',
  description:'',
  maxNum:3,
  expireTime:'',
  password:'',
  status:0
})

myAxios.get("/getTeam",{
  params:{
     id
  }})
    .then(res=>{
      console.log(res.data)
      formData.value=res.data;
    })
const showPicker = ref(false);
const result = ref('');
const onConfirm = ({ selectedValues }) => {
  result.value = selectedValues.join('/');
  showPicker.value = false;
};


const onSubmit = (values) => {
  console.log(formData.value)
  myAxios.post("/updateTeam",formData.value)
      .then(res=>{
        showToast(res.data);
      })
};
</script>

<template>
  <div>
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
            v-model="formData.name"
            name="队伍名"
            label="队伍名"
            placeholder="队伍名"
            :rules="[{ required: true, message: '请填写队伍名' }]"
        />
        <van-field
            v-model="formData.description"
            name="队伍描述"
            label="队伍描述"
            placeholder="队伍描述"
            :rules="[{ required: true, message: '请填写队伍描述' }]"
        />


        <van-field name="stepper" label="队伍人数">
          <template #input>
            <van-stepper v-model="formData.maxNum" max="10" min="3" />
          </template>
        </van-field>
        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="formData.status" direction="horizontal">
              <van-radio name="1">公开</van-radio>
              <van-radio name="2">私有</van-radio>
              <van-radio name="3">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
            v-if="formData.status==3"
            v-model="formData.password"
            name="密码"
            label="密码"
            placeholder="密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block  type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>

  </div>
</template>

<style scoped>

</style>