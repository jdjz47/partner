<script setup >
import {useRoute, useRouter} from "vue-router";
import myAxios from '/src/plusing/myAxios.js'
import {ref} from "vue";
import i from './1.jpg'
import {showSuccessToast, showToast} from "vant";
const route=useRoute();
const router=useRouter();
let tagList=route.query.tags;
console.log(tagList)
const value = ref('');

let teamList=ref([{
  name:'屠龙战队',
  description:'超级战斗',
  status:0,
  maxNum:5,
  expireTime:'',
  role:0
}]);
myAxios.get('/list',{
  params:{
    pageNum: 1,
    pageSize: 8
  }
}).then(response=>{
  console.log(response)
  let data=response.data.records;
  teamList.value=data;
  console.log(data,"data")
  console.log(teamList.value,"teamList")
})
const joinTeam=(team)=>{
  myAxios.post("join",team)
      .then(res=>{
        console.log(res.data)
        showToast({
          message: res.data,
          className: 'particulars-detail-popup'
        })
      })
}
const updateTeam=(team)=>{
  router.push({
    name: '/updateTeam',
    query:{
      id:team.id
    }
  })
}

const onSearch = (val) =>{
  myAxios.get('/list',{
    params:{
      pageNum: 1,
      pageSize: 8,
      name:val
    }
  }).then(response=>{
    console.log(response)
    let data=response.data.records;
    teamList.value=data;
    console.log(data,"data")
    console.log(teamList.value,"teamList")
  })
}
const onCancel = () => showToast('取消')
</script>

<template>
  <van-search
      v-model="value"
      placeholder="请输入搜索关键词"
      @search="onSearch"
      @cancel="onCancel"
  />

  <van-card
      v-for="team in teamList"
      :desc="team.description"
      :title="`${team.name} (${team.name})`"
      :thumb="i"
  >
    <template #tags>
      <van-tag plain v-if="team.status=='0'" style="margin-right: 8px" type="primary">公开</van-tag>
      <van-tag plain v-if="team.status=='1'" style="margin-right: 8px" type="primary">私有</van-tag>
      <van-tag plain v-if="team.status=='2'" style="margin-right: 8px" type="primary">加密</van-tag>
    </template>
    <template #footer>
      <van-button size="mini" type="primary" v-on:click="joinTeam(team)">加入队伍</van-button>
      <van-button v-if="team.role=='1'" size="mini" type="primary" v-on:click="updateTeam(team)">更新队伍</van-button>
    </template>
    <template #bottom>
        <div>
            {{team.num}}名成员
        </div>
      <div>
      最大人数{{team.maxNum}}
      </div>
      <div>
        队伍过期时间{{new Date(team.expireTime)}}
      </div>
    </template>
  </van-card>
</template>

<style scoped>
:deep(.van-image__img){
  height: 128px;
  object-fit: unset;
}
.particulars-detail-popup{
  background: rgba(0, 0, 0, 0.7) !important;
}
</style>