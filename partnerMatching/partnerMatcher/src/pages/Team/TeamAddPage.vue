<script setup>
import {ref} from "vue";
import myAxios from "../../plusing/myAxios.js";
import {showToast} from "vant";


const formData=ref({
  name:'',
  description:'',
  maxNum:3,
  expireTime:'',
  password:'',
  status:0
})
const showPicker = ref(false);
const result = ref('');
const onConfirm = ({ selectedValues }) => {
  result.value = selectedValues.join('/');
  showPicker.value = false;
};
   let showDatePopup=ref(false);
   let showTimePopup=ref(false);
   let date='';
   let time='';

   let currentDate=[];
   let currentTime=[];
const onDateConfirm=()=>{
  showDatePopup.value = false
  date = currentDate[0] + '-' + currentDate[1] + '-' + currentDate[2]
};

const onTimeConfirm=()=>{
  showTimePopup.value = false
  time = currentTime[0] + ':' + currentTime[1];
  let d= date + ' ' + time + '00'
  const dates=new Date(d);
  formData.value.expireTime=dates;
  console.log(formData.value.expireTime)
};
const onSubmit = (values) => {
    console.log(formData.value)
  myAxios.post("/addTeam",formData.value)
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
      <van-field
          v-model="date"
          is-link
          name="日期"
          label="日期"
          placeholder="请选择日期"
          @click="showDatePopup = true"
      />
      <van-popup v-model:show="showDatePopup" round position="bottom">
        <van-date-picker v-model="currentDate" type="datetime" title="选择日期" @confirm="onDateConfirm" />
      </van-popup>

      <van-field
          v-model="time"
          is-link
          name="时间"
          label="时间"
          placeholder="请选择过期时间"
          @click="showTimePopup = true"
      />
      <van-popup v-model:show="showTimePopup" round position="bottom">
        <van-time-picker v-model="currentTime" title="选择时间" @confirm="onTimeConfirm" />
      </van-popup>

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