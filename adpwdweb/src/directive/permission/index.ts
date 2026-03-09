import type { Directive, DirectiveBinding } from "vue";

import { useUserStore } from "@/store";

/**
 * 按鈕權限
 */
export const hasPerm: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const requiredPerms = binding.value;

    // 校驗傳入的權限值是否合法
    if (!requiredPerms || (typeof requiredPerms !== "string" && !Array.isArray(requiredPerms))) {
      throw new Error(
        "需要提供權限標識！例如：v-has-perm=\"'sys:user:add'\" 或 v-has-perm=\"['sys:user:add', 'sys:user:edit']\""
      );
    }

    const { roles, perms } = useUserStore().userInfo;

    // 超級管理員擁有所有權限
    if (roles.includes("ROOT")) {
      return;
    }

    // 檢查權限
    const hasAuth = Array.isArray(requiredPerms)
      ? requiredPerms.some((perm) => perms.includes(perm))
      : perms.includes(requiredPerms);

    // 如果沒有權限，移除該元素
    if (!hasAuth && el.parentNode) {
      el.parentNode.removeChild(el);
    }
  },
};

/**
 * 角色權限指令
 */
export const hasRole: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const requiredRoles = binding.value;

    // 校驗傳入的角色值是否合法
    if (!requiredRoles || (typeof requiredRoles !== "string" && !Array.isArray(requiredRoles))) {
      throw new Error(
        "需要提供角色標識！例如：v-has-role=\"'ADMIN'\" 或 v-has-role=\"['ADMIN', 'TEST']\""
      );
    }

    const { roles } = useUserStore().userInfo;

    // 檢查是否有對應角色權限
    const hasAuth = Array.isArray(requiredRoles)
      ? requiredRoles.some((role) => roles.includes(role))
      : roles.includes(requiredRoles);

    // 如果沒有權限，移除元素
    if (!hasAuth && el.parentNode) {
      el.parentNode.removeChild(el);
    }
  },
};
