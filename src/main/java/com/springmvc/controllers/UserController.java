package com.springmvc.controllers;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.springmvc.forms.UserEditForm;
import com.springmvc.forms.UserForm;
import com.springmvc.models.Person;
import com.springmvc.models.Role;
import com.springmvc.models.User;
import com.springmvc.repositories.PersonRepository;
import com.springmvc.repositories.RoleRepository;
import com.springmvc.repositories.UserRepository;
import com.springmvc.utils.DateTimeUtil;
import com.springmvc.validators.UserValidator;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired UserRepository userRepository;
	@Autowired RoleRepository roleRepository;
	@Autowired PersonRepository personRepository;
	@Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired UserValidator userValidtor;
	@Autowired EntityManager entityManager;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/users/index");
		
		// page number
		String pageNumStr = request.getParameter("page");
		int pageNum = pageNumStr == null? 0 : Integer.parseInt(pageNumStr) - 1;
		
		Pageable pageable = new PageRequest(pageNum, 5, Sort.Direction.ASC, "username");
		Page<User> page = userRepository.findList(pageable);
		
		//List<User> page = entityManager.createQuery("SELECT u, u.person FROM User u JOIN fetch u.person").getResultList();
		
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView show(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/users/show");
		
		User user = userRepository.findOne(id);
		
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/users/add", "user", new UserForm());
		
		Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		Iterable<Person> people = personRepository.findAll();
		
		mv.addObject("roles", roles);
		mv.addObject("people", people);
		return mv;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> create(HttpServletRequest request, @Valid UserForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		userValidtor.validate(form, result);
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Iterable<Role> roles = roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		
		// add a user
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		user.setRoles(Sets.newHashSet(roles));
		user.setPerson(personRepository.findOne(form.getPerson()));
		userRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(user.getId());
	}
	
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(HttpServletRequest request, @PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView("/users/edit");
		
		User user = userRepository.findOne(id);
		Iterable<Role> roles = roleRepository.findAll();
		Iterable<Person> people = personRepository.findAll();
		
		mv.addObject("user", user);
		mv.addObject("roles", roles);
		mv.addObject("people", people);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid UserEditForm form, BindingResult result, final RedirectAttributes redirectAttr) {
		
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result.getFieldErrors());
		}
		
		Iterable<Role> roles = roleRepository.findAll(form.getRoles());
		
		User user = userRepository.findOne(id);
		BeanUtils.copyProperties(form, user);
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		user.setRoles(Sets.newHashSet(roles));
		user.setPerson(personRepository.findOne(form.getPerson()));
		userRepository.save(user);
		
		return ResponseEntity.status(HttpStatus.OK).body(user.getId());
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id, final RedirectAttributes redirectAttr) {
		
		userRepository.delete(id);
		
		redirectAttr.addFlashAttribute("notice", "Delete success!");
		
		return new ModelAndView("redirect:/users");
	}
	
	@RequestMapping("/save-fail-test")
	@ResponseBody
	@Transactional
	public Map<String, Object> saveFailTest(HttpServletRequest request) throws Exception {
		// add a user
		User user = new User();
		user.setUsername("lily");
		user.setPassword("password");
		user.setCreatedAt(DateTimeUtil.getCurrTimestamp());
		user.setUpdatedAt(DateTimeUtil.getCurrTimestamp());
		userRepository.save(user);
		
		if (user.getId() > 0) throw new RuntimeException("Please rollback!");
		
		return Maps.newHashMap();
	}
	
}