<script setup lang="ts">

import {onBeforeMount} from "vue";
import {container} from "tsyringe";
import UserRepository from "@/repository/UserRepository";

const USER_REPOSITORY = container.resolve(UserRepository);

onBeforeMount(() => {
  USER_REPOSITORY.getProfile().then((user) => {
    console.log(user);
  })
})
</script>

<template>
  <ul class="menus">
    <li class="menu">
      <router-link to="/">처음으로</router-link>
    </li>
    <li class="menu">
      <router-link to="/login">로그인</router-link>
    </li>
    <li class="menu">
      <router-link to="/write">글 작성</router-link>
    </li>
  </ul>

</template>

<style scoped lang="scss">
.menus {
  height: 20px;
  list-style: none;
  padding: 0;
  font-size: 0.88rem;
  font-weight: 300;
  text-align: center;
  margin: 0;
}

.menu {
  display: inline;
  margin-right: 1rem;

  &:last-child {
    margin-right: 0;
  }

  a {
    color: inherit;
  }
}

</style>