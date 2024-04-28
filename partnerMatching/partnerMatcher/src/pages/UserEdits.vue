<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import { ref } from "vue";
import myAxios from "../plusing/myAxios.js";
import {showToast} from "vant";


const route = useRoute();
const router = useRouter();
const editUser = ref({
  editKey: route.query.editKey,
  editValue: route.query.editValue,
  editName: route.query.editName,
})
const keys=editUser.value.editKey;

const onSubmit = (values) => {
  console.log(editUser.value.editValue)
  myAxios.post('/UpdateUser', {
    [keys]:editUser.value.editValue
  }).then(response=>{
    showToast('修改成功');
      router.back();
      console.log(response)
  })
};

</script>

<template>
  <van-form @submit="onSubmit(editUser.editKey)">
    <van-field
        v-model="editUser.editValue"
        :name="editUser.editKey"
        :label="editUser.editName"
        :placeholder="`请输入${editUser.editName}`"
    />
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<style scoped>

</style>