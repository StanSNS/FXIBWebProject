import axios from 'axios';
import {getToken} from './AuthService';

// Axios interceptor to add the Authorization token to all outgoing requests.
axios.interceptors.request.use(
    function (config) {
        config.headers['Authorization'] = getToken();
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

// Function to set whether a user agreed to the terms and conditions.
export const setUserAgreedToTermsAndConditions = (username, jwtToken, isAgreedToTermsAndConditions) => {
    const url = `http://localhost:8000/community?action=setAgreeToTermsAndConditions&username=${username}&&jwtToken=${jwtToken}&isAgreedToTermsAndConditions=${isAgreedToTermsAndConditions}`;
    return axios.post(url).then((response) => {
        if (response.status === 200) {
            return response.data;
        } else {
            throw new Error('Failed to set user agree to terms and conditions');
        }
    }).catch((error) => {
        throw error;
    });
};

// Function to add a new question to the community.
export const addNewQuestion = (username, jwtToken, questionContent, topic) => {
    const url = `http://localhost:8000/community?action=addQuestion&username=${username}&jwtToken=${jwtToken}&questionContent=${questionContent}&topic=${topic}`;
    return axios.post(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to add new question');
            }
        }).catch((error) => {
            throw error;
        });
};


// Function to add a new answer to a question in the community.
export const addNewAnswerToTheQuestion = (username, jwtToken, answerContent, questionID) => {
    const url = `http://localhost:8000/community?action=addNewAnswer&username=${username}&jwtToken=${jwtToken}&answerContent=${answerContent}&questionID=${questionID}`;
    return axios.post(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to add new answer to the question');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to get all questions for a specific topic.
export const getAllQuestions = (topic, username, jwtToken) => {
    const url = `http://localhost:8000/community?action=getAllQuestions&topic=${topic}&username=${username}&jwtToken=${jwtToken}`;
    return axios.get(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data
            } else {
                throw new Error('Failed to get all questions');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to get whether a user agreed to the terms and conditions.
export const getUserAgreedToTermsAndConditions = (username, jwtToken) => {
    return axios.get(`http://localhost:8000/community?action=getUserAgreedToTermsAndConditions&username=${username}&jwtToken=${jwtToken}`,
    ).then((response) => {
        if (response.status === 200) {
            return response.data
        } else {
            throw new Error('Failed to get user data');
        }
    }).catch((error) => {
        throw error;
    });
};

// Function to increase the vote count for an answer.
export const increaseAnswerVoteCount = (username, jwtToken, answerID) => {
    const url = `http://localhost:8000/community?action=increaseVoteCount&username=${username}&jwtToken=${jwtToken}&answerID=${answerID}`;
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to increase vote count');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to decrease the vote count for an answer.
export const decreaseAnswerVoteCount = (username, jwtToken, answerID) => {
    const url = `http://localhost:8000/community?action=decreaseVoteCount&username=${username}&jwtToken=${jwtToken}&answerID=${answerID}`;
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to decrease vote count');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to mark a question as solved.
export const setSolvedQuestion = (username, jwtToken, questionID) => {
    const url = `http://localhost:8000/community?action=solveQuestion&username=${username}&jwtToken=${jwtToken}&questionID=${questionID}`;
    return axios.put(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to set solved question');
            }
        }).catch((error) => {
            throw error;
        });
};

// Function to get all topic names.
export const getAllTopics = () => {
    return axios.get(`http://localhost:8000/community?action=getAllTopicNames`);
};

// Function to delete a question by its ID.
export const deleteQuestionID = (username, jwtToken, questionID) => {
    const url = `http://localhost:8000/community/deleteQuestion?username=${username}&jwtToken=${jwtToken}&questionID=${questionID}`;
    return axios.delete(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to set solved question');
            }
        }).catch((error) => {
            throw error;
        });
};


// Function to delete an answer by its ID.
export const deleteAnswerID = (username, jwtToken, answerID) => {
    const url = `http://localhost:8000/community/deleteAnswer?username=${username}&jwtToken=${jwtToken}&answerID=${answerID}`;
    return axios.delete(url)
        .then((response) => {
            if (response.status === 200) {
                return response.data;
            } else {
                throw new Error('Failed to set solved question');
            }
        }).catch((error) => {
            throw error;
        });
};