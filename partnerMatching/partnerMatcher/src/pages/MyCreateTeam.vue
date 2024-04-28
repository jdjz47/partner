<script setup>
import i from "./Team/1.jpg";
import {useRoute, useRouter} from "vue-router";
import myAxios from '/src/plusing/myAxios.js'
import {ref} from "vue";
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
  num:0
}]);
myAxios.get('/MyCreateTeamList',{
  params:{
    pageNum: 1,
    pageSize: 8
  }
}).then(response=>{
  console.log(response)
  let data=response.data;
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
const QuitTeam=(team)=>{
  myAxios.post("quit",team)
      .then(res=>{
        console.log(res.data)
        showToast({
          message: res.data,
          className: 'particulars-detail-popup'
        })
      })
}
const DisBandTeam=(team)=>{
  myAxios.get('/delTeam',{
    params:{
      id:team.id
    }
  }).then(res=>{
    showToast({
      message: res.data,
      className: 'particulars-detail-popup'
    })
  })
}



</script>

<template>
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
      <van-button size="mini" type="primary" v-on:click="updateTeam(team)">更新队伍</van-button>
      <van-button size="mini" type="primary" v-on:click="QuitTeam(team)">退出队伍</van-button>
      <van-button size="mini" type="primary" v-on:click="DisBandTeam(team)">解散队伍</van-button>
    </template>
    <template #bottom>
        <div>
            {{team.num}}名成员
        </div>
      <div>
        最大人数{{team.maxNum}}
      </div>
      <div>
        队伍过期时间{{team.expireTime}}
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