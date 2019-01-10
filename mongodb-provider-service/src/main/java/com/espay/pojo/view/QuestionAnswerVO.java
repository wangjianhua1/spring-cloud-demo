package com.espay.pojo.view;

public class QuestionAnswerVO {
	private String id;
	private String[] questions;
	private String answer;
	private String mainQuestion;
	private String userId;
	private String userName;
	private String status;
	private String knowledgeClass;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getQuestions() {
		return questions;
	}

	public void setQuestions(String[] questions) {
		this.questions = questions;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getMainQuestion() {
		return mainQuestion;
	}

	public void setMainQuestion(String mainQuestion) {
		this.mainQuestion = mainQuestion;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKnowledgeClass() {
		return knowledgeClass;
	}

	public void setKnowledgeClass(String knowledgeClass) {
		this.knowledgeClass = knowledgeClass;
	}
}
