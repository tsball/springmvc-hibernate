package com.springmvc.models;

/**
 * 这里定义了所有角色的身份。
 * 不建议定义通配类型角色，因为每个角色在整个系统的业务流程都是扮演者不同的作用。
 * 
 * 对于有业务流程需求的系统，角色应该是明确的。因为它的角色是有上下级关系的。
 * 通配类型的角色，角色之间是平行的关系，只是有不同的属性而已。当使用activiti时，bpmn中（因为candidateGroups）也需要动态生成。
 * 通配类型的角色，对角色拓展是比较灵活，但是对系统的复制性增加很多，因为所有角色都没有实际意义，它的意义需要在系统中进行配置才能体现出来(如把某个角色绑定了请求流程的发起人，那么这个角色就可以理解为员工)。
 * 这也是为什么spring security大部分解决方案都是hard code url->role
 * 
 * RoleVoter requires the code with 'ROLE_' as prefix.
 */
public enum RoleCode {
	// ROLE_ANONYMOUS,
	ROLE_EMPLOYEE, 
	ROLE_DEPARTMENT_MANAGER, 
	ROLE_HR, 
	ROLE_MANAGER, 
	ROLE_ADMIN
}
