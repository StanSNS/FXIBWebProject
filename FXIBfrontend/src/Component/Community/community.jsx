import React, {useEffect, useRef, useState} from 'react';
import './community.css';
import {
    FaChevronDown,
    FaChevronUp,
    FaExclamationCircle,
    FaExclamationTriangle,
    FaHeart,
    FaIdBadge,
    FaPlus,
    FaReply,
    FaSearch,
    FaSortAlphaDown,
    FaSortAlphaUp,
    FaSortAmountDown,
    FaSortAmountUp,
    FaSortNumericDownAlt,
    FaSortNumericUp,
    FaTimes
} from 'react-icons/fa';
import {Link} from "react-router-dom";
import {Button, Dropdown, Modal} from "react-bootstrap";
import {MdHeartBroken} from "react-icons/md";
import {
    addNewAnswerToTheQuestion,
    addNewQuestion,
    decreaseAnswerVoteCount,
    deleteAnswerID,
    deleteQuestionID,
    getAllQuestions,
    getAllTopics,
    getUserAgreedToTermsAndConditions,
    increaseAnswerVoteCount,
    setSolvedQuestion,
    setUserAgreedToTermsAndConditions
} from "../../Service/CommunityService";
import {getToken, isAdministrator, isUserBanned, isUserLoggedIn, loggedUserUsername} from "../../Service/AuthService";
import {BiSolidBadgeDollar} from "react-icons/bi";
import {IoCalendarSharp} from "react-icons/io5";
import {BsTrash3Fill} from "react-icons/bs";
import CommunitySkeleton from "../../SkeletonLoader/CommunitySkeleton";


const Community = () => {

    // State variables
    const [isLoading, setIsLoading] = useState(true); // Initializing state variable isLoading with initial value true
    const [allQuestions, setAllQuestions] = useState([]); // Stores the list of all questions.
    const [dropdownOptions, setDropdownOptions] = useState([]); // Stores the dropdown options for topics.
    const [newQuestionContent, setNewQuestionContent] = useState(''); // Stores the content of the new question.
    const [answerInputs, setAnswerInputs] = useState({}); // Stores answers for questions.
    const [searchQuery, setSearchQuery] = useState(''); // Stores the search query.
    const [isOpen, setIsOpen] = useState(false); // Manages the visibility of a dropdown.
    const [isOverlayVisible, setIsOverlayVisible] = useState(false); // Manages the visibility of an overlay.
    const [selectedTopic, setSelectedTopic] = useState(null); // Stores the selected topic.
    const [isTermsModalVisible, setIsTermsModalVisible] = useState(false); // Manages the visibility of terms modal.
    const [userDetails, setUserDetails] = useState({}); // Stores user details.
    const isAuth = isUserLoggedIn(); // Checks if the user is logged in.
    const isBanned = isUserBanned(); // Checks if the user is banned.
    const isButtonDisabled = !userDetails.agreedToTerms || !isAuth || isBanned; // Checks if a button should be disabled.
    const filteredQuestions = allQuestions.filter((question) => question.content.toLowerCase().includes(searchQuery.toLowerCase())); // Filters questions based on the search query.
    const dropdownRef = useRef(null); // Ref for the dropdown element.
    const [questionErrorMessage, setQuestionErrorMessage] = useState(""); // Stores error messages related to questions.
    const [answerInputErrors, setAnswerInputErrors] = useState({}); // Stores error messages related to answer inputs.
    const [questionCharacterCount, setQuestionCharacterCount] = useState(1500); // Character count limit for a new question.
    const characterCountClassNameQuestion = questionCharacterCount < 0 ? 'text-danger' : 'text-success'; // CSS class for character count styling.
    const [answerCharacterCount, setAnswerCharacterCount] = useState(1500); // Character count limit for an answer.
    const characterCountClassNameAnswer = answerCharacterCount < 0 ? 'text-danger' : 'text-success'; // CSS class for character count styling.
    const [selectedQuestionId, setSelectedQuestionId] = useState(null); // ID of the selected question.
    const [selectedAnswerId, setSelectedAnswerId] = useState(null); // ID of the selected answer.
    const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false); // Manages the visibility of the delete confirmation modal.


    // Fetch topics and set dropdown options.
    useEffect(() => {
        getAllTopics()
            .then((response) => {
                if (response.status === 200) {
                    setDropdownOptions(response.data);
                }
            })
            .catch((er) => {
                console.error(er);
            });
    }, []);

    // Fetch topics, set dropdown options, and load questions for the initial topic.
    useEffect(() => {
        getAllTopics()
            .then((response) => {
                if (response.status === 200) {
                    setDropdownOptions(response.data);
                    if (response.data.length > 0 && selectedTopic === null) {
                        setSelectedTopic(response.data[0]);
                        fetchQuestionsByTopic(response.data[0]);
                    }
                }
            })
            .catch((er) => {
                console.error(er);
            });
    }, [selectedTopic]);

    // Fetch user data and display terms modal if needed.
    useEffect(() => {
        if (isAuth) {
            getUserAgreedToTermsAndConditions(loggedUserUsername(), getToken().substring(7))
                .then((data) => {
                    if (data.agreedToTerms === false) {
                        if (!isBanned) {
                            handleShowTerms();
                        }
                    }
                    setUserDetails(data);
                })
                .catch((error) => {
                    console.error('Failed to get user data' + error)
                });
        }
    }, [isAuth, isBanned]);

    // Add event listener for clicking outside the dropdown.
    useEffect(() => {
        document.addEventListener('click', closeDropdown);
        return () => {
            // Remove event listener when the component unmounts.
            document.removeEventListener('click', closeDropdown);
        };
    }, []);

    // Handles user acceptance of terms and conditions.
    const handleAcceptTerms = () => {
        setUserAgreedToTermsAndConditions(loggedUserUsername(), getToken().substring(7), true)
            .then((data) => {
                handleHideTerms();
                setUserDetails(data)
            })
            .catch((error) => {
                console.error('Failed to set user agree to terms and conditions' + error)
            });
    };

    // Shows the terms modal.
    const handleShowTerms = () => {
        setIsTermsModalVisible(true);
    };

    // Hides the terms modal.
    const handleHideTerms = () => {
        setIsTermsModalVisible(false);
    };

    // Fetches questions based on the selected topic.
    const fetchQuestionsByTopic = (topic) => {
        getAllQuestions(topic, loggedUserUsername(), getToken())
            .then((data) => {
                setAllQuestions(data);
                setNewQuestionContent("");
                setAnswerInputs({});
                setAnswerCharacterCount(1500);
                setQuestionCharacterCount(1500);
                setIsLoading(false);
            })
            .catch(() => {
            });
    };

    // Handles the change of the selected topic.
    const handleTopicChange = (newTopic) => {
        setSelectedTopic(newTopic);
        fetchQuestionsByTopic(newTopic);
    };

    // Handles liking or unliking an answer.
    const handleLike = (questionId, answerId) => {
        if (!userDetails.agreedToTerms) {
            return;
        }
        const updatedQuestions = allQuestions.map((question) => {
            if (question.id === questionId) {
                const updatedAnswers = question.answers.map((answer) => {
                    if (answer.id === answerId) {
                        if (!answer.liked) {
                            increaseAnswerVoteCount(loggedUserUsername(), getToken().substring(7), answerId)
                            return {...answer, voteCount: answer.voteCount + 1, liked: true};
                        } else {
                            decreaseAnswerVoteCount(loggedUserUsername(), getToken().substring(7), answerId)
                            return {...answer, voteCount: answer.voteCount - 1, liked: false};
                        }
                    }
                    return answer;
                });
                return {...question, answers: updatedAnswers};
            }
            return question;
        });
        setAllQuestions(updatedQuestions);
    };

    // Toggles the visibility of answers for a question.
    const toggleAnswers = (questionId) => {
        const updatedQuestions = allQuestions.map((q) => {
            if (q.id === questionId) {
                return {...q, showAnswers: !q.showAnswers};
            }
            return q;
        });
        setAllQuestions(updatedQuestions);
    };

    // Handles adding a new question.
    const handleAddQuestion = () => {
        if (newQuestionContent.trim() === '') {
            setQuestionErrorMessage("Question must not be empty.")
            return;
        }
        if (newQuestionContent.length > 1500) {
            setQuestionErrorMessage("Question must be max 1500 characters.")
            return;
        }
        addNewQuestion(loggedUserUsername(), getToken().substring(7), newQuestionContent, selectedTopic)
            .then((data) => {
                const updatedQuestions = [...allQuestions, data];
                setAllQuestions(updatedQuestions);
                setNewQuestionContent('');
                setQuestionErrorMessage("")
                setQuestionCharacterCount(1500);

            })
            .catch((error) => {
                console.error('Failed to add new question' + error);
            });
    };

    // Handles adding a new answer to a question.
    const handleAddAnswer = (questionId) => {
        if (answerInputs[questionId]?.trim() === '' || answerInputs[questionId] === undefined) {
            setAnswerInputErrors({...answerInputErrors, [questionId]: "Answer must not be empty."});
            return;
        }
        if (answerInputs[questionId]?.length > 1500) {
            setAnswerInputErrors({...answerInputErrors, [questionId]: "Answer must be max 1500 characters."});
            return;
        }
        addNewAnswerToTheQuestion(loggedUserUsername(), getToken().substring(7), answerInputs[questionId], questionId)
            .then((data) => {
                const updatedQuestions = allQuestions.map((question) => {
                    if (question.id === questionId) {
                        if (question.answers) {
                            return {
                                ...question,
                                answers: [...question.answers, data],
                            };
                        } else {
                            return {
                                ...question,
                                answers: [data],
                            };
                        }
                    }
                    return question;
                });
                setAnswerCharacterCount(1500);
                setAnswerInputErrors({...answerInputErrors, [questionId]: ""});
                setAllQuestions(updatedQuestions);
                setAnswerInputs((prevInputs) => ({...prevInputs, [questionId]: ""}));
            })
            .catch((error) => {
                console.error("Failed to add new answer to the question" + error);
            });
    };

    // Toggles the dropdown visibility.
    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    // Closes the dropdown when clicking outside of it.
    const closeDropdown = (event) => {
        if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
            setIsOpen(false);
        }
    };

    // Clears the search query.
    const handleClearClick = () => {
        setSearchQuery('');
    };

    // Toggles a question's solved status.
    const toggleSolvedNotSolved = (questionID) => {
        setSolvedQuestion(loggedUserUsername(), getToken().substring(7), questionID)
            .then(() => {
                const updatedQuestions = [...allQuestions];
                const questionIndex = updatedQuestions.findIndex((question) => question.id === questionID);
                if (questionIndex !== -1) {
                    updatedQuestions[questionIndex] = {
                        ...updatedQuestions[questionIndex],
                        solved: true,
                    };
                    setAllQuestions(updatedQuestions);
                }
            })
            .catch((error) => {
                console.error("Failed to update user solved question" + error);
            });
    };


    // Handles input change for a new question.
    const handleQuestionInputChange = (e) => {
        const inputValue = e.target.value;
        setNewQuestionContent(inputValue);
        const remainingCharacters = 1500 - inputValue.length;
        setQuestionCharacterCount(remainingCharacters);
    };

    // Handles input change for an answer to a question.
    const handleAnswerInputChange = (e, questionId) => {
        const inputValue = e.target.value;
        setAnswerInputs((prevInputs) => ({...prevInputs, [questionId]: inputValue,}));
        const remainingCharacters = 1500 - inputValue.length;
        setAnswerCharacterCount(remainingCharacters);
    };

    // Handles deleting a question.
    const handleDeleteQuestion = () => {
        deleteQuestionID(loggedUserUsername(), getToken().substring(7), selectedQuestionId)
            .then(() => {
                if (selectedQuestionId) {
                    const updatedQuestions = allQuestions.filter((question) => question.id !== selectedQuestionId);
                    setAllQuestions(updatedQuestions);
                }
                setIsDeleteModalVisible(false);
                setSelectedQuestionId(null);
            })
        if (selectedQuestionId) {
            const updatedQuestions = allQuestions.filter((question) => question.id !== selectedQuestionId);
            setAllQuestions(updatedQuestions);
        }
        setIsDeleteModalVisible(false);
        setSelectedQuestionId(null);
    };

    // Handles deleting an answer.
    const handleDeleteAnswer = () => {
        deleteAnswerID(loggedUserUsername(), getToken().substring(7), selectedAnswerId)
            .then(() => {
                if (selectedAnswerId) {
                    const updatedQuestions = allQuestions.map((question) => {
                        const updatedAnswers = question.answers.filter((answer) => answer.id !== selectedAnswerId);
                        return {...question, answers: updatedAnswers};
                    });
                    setAllQuestions(updatedQuestions);
                }
                setIsDeleteModalVisible(false);
                setSelectedAnswerId(null);
            })
    };

    // Closes the delete modal.
    const handleCloseDeleteModal = () => {
        setSelectedQuestionId(null);
        setSelectedAnswerId(null);
        setIsDeleteModalVisible(false);
    };

    // Sorts questions based on the selected sorting option.
    const handleSortByQuestions = (sortByOption) => {
        const sortedQuestions = sortQuestions(allQuestions, sortByOption);
        setAllQuestions(sortedQuestions);
    };

    // Helper function to sort questions.
    const sortQuestions = (questions, sortByOption) => {
        const sortedQuestions = [...questions];
        switch (sortByOption) {
            case 'Oldest':
                sortedQuestions.sort((a, b) => new Date(a.date) - new Date(b.date));
                break;
            case 'Newest':
                sortedQuestions.sort((a, b) => new Date(b.date) - new Date(a.date));
                break;
            case 'Alphabetically A-Z':
                sortedQuestions.sort((a, b) => a.content.localeCompare(b.content));
                break;
            case 'Alphabetically Z-A':
                sortedQuestions.sort((a, b) => b.content.localeCompare(a.content));
                break;
            default:
                break;
        }
        return sortedQuestions;
    };

    // Sorts answers of questions based on the selected sorting option.
    const handleSortAnswers = (sortByOption) => {
        const sortedQuestions = [...allQuestions];
        sortedQuestions.forEach((question) => {
            question.answers.sort((a, b) => {
                switch (sortByOption) {
                    case 'Newest':
                        return new Date(b.date) - new Date(a.date);
                    case 'Oldest':
                        return new Date(a.date) - new Date(b.date);
                    case 'Likes 1-9':
                        return a.voteCount - b.voteCount;
                    case 'Likes 9-1':
                        return b.voteCount - a.voteCount;
                    case 'Alphabetically A-Z':
                        return a.content.localeCompare(b.content);
                    case 'Alphabetically Z-A':
                        return b.content.localeCompare(a.content);
                    default:
                        return 0;
                }
            });
        });
        setAllQuestions(sortedQuestions);
    };

    return (
        <div className="question-list addMarginTop">
            <h2 className="text-center "><span className="colorBlueText">#</span>{selectedTopic}</h2>
            <div className="questionNumberStyling mb-5 text-center">
                <strong>Found: ({filteredQuestions.length})</strong>
            </div>
            <span className="text-danger font-weight-bolder ml-2">{questionErrorMessage}</span>
            <div className="new-question-search-topics-container mb-3">
                <div className="new-question ml-2">
                    <input
                        className={isButtonDisabled ? 'disabled' : ''}
                        type="text"
                        placeholder="Enter your question... (max. 1500 characters)"
                        value={newQuestionContent}
                        disabled={isButtonDisabled}
                        onChange={handleQuestionInputChange}
                    />
                    <span
                        className={`character-count ml-3 mr-3 font-weight-bolder ${characterCountClassNameQuestion}`}>({questionCharacterCount})</span>
                    <button
                        className={`communityButton align-bottom ml-2 ${isButtonDisabled ? 'disabled' : ''}`}
                        onClick={handleAddQuestion}
                        disabled={isButtonDisabled}>
                        Add Question <FaPlus className="ml-1 mb-1"/>
                    </button>
                </div>
                <div className="mr-5">
                    <Dropdown>
                        <Dropdown.Toggle variant="light" id="dropdown-basic" className="communityButton">
                            Sort By
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                            <Dropdown.Item onClick={() => handleSortByQuestions('Newest')}>
                                Newest <span className="align-text-bottom"><FaSortAmountUp/></span>
                            </Dropdown.Item>
                            <Dropdown.Item onClick={() => handleSortByQuestions('Oldest')}>
                                Oldest <span className="align-text-bottom"><FaSortAmountDown/></span>
                            </Dropdown.Item>
                            <Dropdown.Item onClick={() => handleSortByQuestions('Alphabetically A-Z')}>
                                Alphabetically A-Z <span className="align-text-bottom"><FaSortAlphaDown/></span>
                            </Dropdown.Item>
                            <Dropdown.Item onClick={() => handleSortByQuestions('Alphabetically Z-A')}>
                                Alphabetically Z-A <span className="align-text-bottom"><FaSortAlphaUp/></span>
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
                <div className="search-bar mt-2 text-right mr-3">
                    <div className="input-container">
                        <span className="mr-3"><FaSearch/></span>
                        <input
                            className="customSearchWidth mr-2"
                            type="text"
                            placeholder="Search questions..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}/>
                        {searchQuery && (
                            <button className="btn btn-secondary" onClick={handleClearClick}>
                                Clear <FaTimes/>
                            </button>
                        )}
                    </div>
                </div>
                <div className="dropdown" ref={dropdownRef}>
                    <button
                        className="communityButton align-bottom ml-2 dropdown-toggle"
                        type="button"
                        onClick={() => {
                            toggleDropdown();
                            setIsOverlayVisible(!isOpen);
                        }}>
                        Topics
                    </button>
                    <div className={`dropdown-menu${isOpen ? ' show' : ''} mt-1`}>
                        {dropdownOptions.map((option, index) => (
                            <Link to={"#"}
                                  className="dropdown-item"
                                  key={index}
                                  onClick={() => {
                                      toggleDropdown();
                                      setIsOverlayVisible(false);
                                      setSelectedTopic(option);
                                      handleTopicChange(option);
                                  }}>
                                <span className="colorBlueText">#</span> {option}
                            </Link>
                        ))}
                    </div>
                </div>
                <div
                    className={`overlay${isOverlayVisible ? ' visible' : ''}`}
                    onClick={() => setIsOverlayVisible(false)}
                ></div>
            </div>


            {isLoading && <CommunitySkeleton repeat={3}/>}


            {filteredQuestions.length === 0
                ? (<div>
                    <br/>
                    <h4 className="text-center">
                        <strong>No match found </strong> <FaExclamationTriangle className="mb-1 ml-1"/>
                    </h4>
                </div>)
                : (filteredQuestions.map((question) => (
                        <div key={question.id} className="question ">
                            <div className="d-flex justify-content-between">
                                <h5 className="question-content">{question.content}</h5>
                                <div className="d-flex">
                                    {isUserLoggedIn() && isAdministrator() &&
                                        <span>
                                             <button
                                                 className="CustomBinIcon mr-2 mt-1"
                                                 onClick={() => {
                                                     setSelectedQuestionId(question.id);
                                                     setIsDeleteModalVisible(true);
                                                 }}>
                                                 <BsTrash3Fill/>
                                             </button>
                                        </span>
                                    }
                                    <span className="customMessageStyle ">
                                        {question.solved === false
                                            ? <span className="notSolvedText">Not Solved</span>
                                            : <span className="solvedText">Solved</span>}
                                    </span>
                                    {question.writer === loggedUserUsername() && question.solved === false &&
                                        <span className="checkbox-wrapper-25 ml-4 mt-2">
                                              <input type="checkbox"
                                                     onClick={() => toggleSolvedNotSolved(question.id)}/>
                                        </span>
                                    }
                                    <button
                                        className="communityButton ml-4 mr-1 fixed-width-button"
                                        onClick={() => toggleAnswers(question.id)}>
                                        {question.showAnswers ? <FaChevronUp/> : <FaChevronDown/>}
                                    </button>
                                </div>
                            </div>
                            <p className="mb-0">
                                <strong> @{question.writer} </strong>
                                <span className="fontColor ml-3 align-text-bottom"><IoCalendarSharp/> </span>
                                <span>{question.date}</span>
                                <strong className="fontColor ml-3 align-text-bottom">
                                    <BiSolidBadgeDollar/></strong> {question.subscriptionPlan}
                            </p>
                            <p>
                                <strong className="fontColor align-text-bottom"><FaIdBadge/> </strong>
                                <span className="bioText">"{question.userBiography}"</span>
                            </p>
                            <span className="text-danger font-weight-bolder">
                                 {answerInputErrors[question.id]}
                            </span>
                            <div className="new-answer mt-0 d-flex">
                                <input
                                    className={`addAnswerField ${isButtonDisabled ? 'disabled' : ''}`}
                                    type="text"
                                    placeholder="Enter your answer... (max. 1500 characters)"
                                    value={answerInputs[question.id] || ''}
                                    disabled={isButtonDisabled}
                                    onChange={(e) => handleAnswerInputChange(e, question.id)}/>
                                <span
                                    className={`ml-2 font-weight-bolder ${characterCountClassNameAnswer} mt-3`}>({answerCharacterCount})</span>
                                <button
                                    className={`communityButton align-bottom ml-3 ${isButtonDisabled ? 'disabled' : ''}`}
                                    onClick={() => handleAddAnswer(question.id)}
                                    disabled={isButtonDisabled}>
                                    Reply <FaReply className="ml-1 mb-1"/>
                                </button>
                                <div className="ml-0">
                                    <Dropdown>
                                        <Dropdown.Toggle variant="light" id="dropdown-basic"
                                                         className="communityButton">
                                            Sort By
                                        </Dropdown.Toggle>
                                        <Dropdown.Menu>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Newest')}>
                                                Newest <span className="align-text-bottom"><FaSortAmountUp/></span>
                                            </Dropdown.Item>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Oldest')}>
                                                Oldest <span className="align-text-bottom"><FaSortAmountDown/></span>
                                            </Dropdown.Item>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Likes 1-9')}>
                                                Likes 1-9 <span className="align-text-bottom"><FaSortNumericUp/></span>
                                            </Dropdown.Item>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Likes 9-1')}>
                                                Likes 9-1 <span
                                                className="align-text-bottom"><FaSortNumericDownAlt/></span>
                                            </Dropdown.Item>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Alphabetically A-Z')}>
                                                Alphabetically A-Z <span
                                                className="align-text-bottom"><FaSortAlphaDown/></span>
                                            </Dropdown.Item>
                                            <Dropdown.Item onClick={() => handleSortAnswers('Alphabetically Z-A')}>
                                                Alphabetically Z-A <span
                                                className="align-text-bottom"><FaSortAlphaUp/></span>
                                            </Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </div>
                                <span className="questionNumberStyling ml-3 mt-1"><strong>Answers: <span
                                    className="colorBlueText"> ({question.answers.length}) </span></strong> </span>
                            </div>
                            <div className={`answers collapse ${question.showAnswers ? 'show' : ''}`}>
                                {question.answers.length === 0 ? (
                                    <div className="no-answers-message mt-1  text-center ">
                                        <span
                                            className="customMesseageNoComments  "> No replies yet <FaExclamationCircle/> </span>
                                    </div>
                                ) : (
                                    <div className="answers-scrollable" style={{maxHeight: '500px', overflowY: 'auto'}}>
                                        {question.answers.map((answer) => (
                                            <div key={answer.id} className="answer whiteColor">
                                                <div className="d-flex justify-content-between">
                                                    <h5 className="answer-content">{answer.content}</h5>
                                                    {isUserLoggedIn() && isAdministrator() &&
                                                        <span>
                                                             <button
                                                                 className="CustomBinIcon"
                                                                 onClick={() => {
                                                                     setSelectedAnswerId(answer.id);
                                                                     setIsDeleteModalVisible(true);
                                                                 }}>
                                                               <BsTrash3Fill/>
                                                              </button>
                                                        </span>
                                                    }
                                                </div>
                                                {allQuestions &&
                                                    <div>
                                                        <strong className="fontColor">Answered by: </strong>
                                                        <strong> @{answer.writer} </strong> on {answer.date}
                                                        <strong
                                                            className="fontColor ml-1"> Subscription:</strong> {answer.userSubscriptionPlan}
                                                        <button
                                                            className="ml-1 likeButton"
                                                            onClick={() => handleLike(question.id, answer.id)}
                                                            disabled={isButtonDisabled}>
                                                            {userDetails.agreedToTerms
                                                                ? (<FaHeart className="mb-1 customHeart"
                                                                            color={answer.liked ? 'red' : 'black'}/>)
                                                                : (<MdHeartBroken className="mb-1"/>
                                                                )}
                                                        </button>
                                                        <span>{answer.voteCount}</span>
                                                        <p className="mb-0">
                                                            <strong className="fontColor">Biography: </strong>
                                                            <span className="bioText">"{answer.userBiography}"</span>
                                                        </p>
                                                    </div>
                                                }
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                    ))
                )}
            <Modal show={isTermsModalVisible} onHide={handleHideTerms} backdrop="static" keyboard={false} centered>
                <div className="modal-content">
                    <Modal.Header className="removeBorder">
                        <Modal.Title>Terms and Conditions</Modal.Title>
                    </Modal.Header>
                    <Modal.Body className="text-center">
                        <p className="insideTextTandC">Are you willing to accept our
                            <Button variant="light ml-0 customTandCButton">
                                <Link to="/risk-disclousure-terms-conditions" target="_blank"
                                      className="customTandCText">
                                    Terms and Conditions
                                </Link>
                            </Button>
                        </p>
                    </Modal.Body>
                    <Modal.Footer className="removeBorder justify-content-center">
                        <Button variant="danger" onClick={handleHideTerms}>
                            No
                        </Button>
                        <Button variant="info" onClick={handleAcceptTerms}>
                            Yes
                        </Button>
                    </Modal.Footer>
                </div>
            </Modal>
            <Modal show={isDeleteModalVisible} onHide={handleCloseDeleteModal} centered>
                <Modal.Header className="removeBorder">
                    <Modal.Title>
                        <div>Confirm Deletion</div>
                    </Modal.Title>

                    <Button variant="link" className="close" onClick={handleCloseDeleteModal}>
                        <FaTimes/>
                    </Button>
                </Modal.Header>
                <Modal.Body className="text-center">
                    {selectedQuestionId ? (
                        <p className="font-weight-bolder">Are you sure you want to delete this <span
                            className="customLinkDesign">QUESTION </span>?</p>
                    ) : (
                        <p className="font-weight-bolder">Are you sure you want to delete this <span
                            className="customLinkDesign">ANSWER </span>?</p>
                    )}
                </Modal.Body>
                <Modal.Footer className="removeBorder justify-content-center ">
                    <Button variant="dark" onClick={handleCloseDeleteModal}>
                        Cancel
                    </Button>
                    {selectedQuestionId ? (
                        <Button variant="danger" onClick={handleDeleteQuestion}>
                            Delete Question
                        </Button>
                    ) : (
                        <Button variant="danger" onClick={handleDeleteAnswer}>
                            Delete Answer
                        </Button>
                    )}
                </Modal.Footer>
            </Modal>
        </div>
    );
};
export default Community;