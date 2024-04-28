<template>
    <van-form @submit="onSubmit">
        <van-cell-group inset>
            <van-field
                    v-model="EditValue"
                    :name="EditName"
                    :label="EditName"
                    :placeholder="EditName"
                    :rules="[{ required: true, message: '请填写用户名' }]"
            />

        </van-cell-group>
        <div style="margin: 16px;">
            <van-button round block type="primary" native-type="submit">
                提交
            </van-button>
        </div>
    </van-form>

</template>

<script setup lang="ts">
  import {useRoute} from "vue-router";

  const route=useRoute();
  console.log(route.query.editKey,'keys')
  console.log(route.query.editName,'names')
  console.log(route.query.editValue,'values')
     let Editkey=route.query.editKey;
     let EditName=route.query.editName;
     let EditValue=route.query.editValue;
  const onSubmit = (values) => {
      console.log('submit', values);
  };
</script>

<style scoped>

</style>