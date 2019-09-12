package com.skillsdevelopment.server;

import com.skillsdevelopment.server.model.Course;
import com.skillsdevelopment.server.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniversityService {
    private final Map<UUID, Student> students;
    private final Map<UUID, Course> courses;

    public UniversityService() {
        students = new HashMap<>();
        courses = Stream.of(
                Course.builder().name("Discrete Mathematics").description("This course is primarily intended for students majoring in Computer Science. The emphasis will be on the development of technical discrete mathematics skills, rather than rigorous proof. Topics will include number systems, sets, logic, induction, elementary counting techniques, relations, functions, matrices, and Boolean algebra.").build(),
                Course.builder().name("Programming Fundamentals").description("This course covers the basics of computer programming and provides a foundation for further learning in this area. It is suited for students who are absolute beginners and as such no previous computer programming knowledge is required to finish this course. The course uses the Python programming language which is very simple and straightforward. Although this course is for beginners, the course will cover abstract concepts which can be applied to almost any programming language, and students are encouraged to pay attention to these, since the way of thinking like a programmer is the most valuable lesson they will learn.").build(),
                Course.builder().name("Computer Systems").description("This course is an introduction to computer systems. In this course we will begin by exploring the internal design and functionality of the most basic computer components. From there, we will use an online hardware simulator to actually “build” a computer and develop an assembler from the ground using concepts we will learn in the class. In the process, we will cover the ideas and techniques used in the design of modern computer hardware and discuss major trade-offs involved in system design as well as future trends in computer architecture and how those trends might affect tomorrow’s computers.").build(),
                Course.builder().name("Calculus").description("This course covers topics such as real numbers, differentiation, continuous functions, integration, limits, analytic geometry and trigonometry.").build(),
                Course.builder().name("Operating Systems").description("This course builds on principles learned in Operating Systems 1 to approach complex computer operating system topics such as networks, parallel computing, remote procedure call, concurrency, transactions, shared memory, message passing, scale, naming, and security.").build(),
                Course.builder().name("Software Engineering").build(),
                Course.builder().name("Algorithms and Data Structures").description("This course introduces the fundamental concepts of data structures and the algorithms that proceed from them. Although this course has a greater focus on theory than application the assignments, examples, and cases introduced throughout the course help to bring the gap between theoretical concepts and real world problem solving. We will be using a software tool that will enhance our understanding of the operation and function of the data structures and algorithms explored throughout the course by visually animating examples of data structures and algorithms so that we can understand their operation. Key topics within this course will include recursion, fundamental data structures (including stacks, queues, linked lists, hash tables, tress, and graphs), and the basics of algorithmic analysis.").build(),
                Course.builder().name("Databases").description("This course will cover server database management, configuration and administration, security mechanisms, backup and recovery, transact SQL Programming, and an introduction to database web-application development.").build(),
                Course.builder().name("Analysis of Algorithms").description("This course builds on knowledge of elementary algorithm analysis gained in Data Structures to further analyze the efficiency of algorithms for sorting, searching, and selection. The course will also introduce algorithm design techniques.").build(),
                Course.builder().name("Web Programming").description("This course builds on the concepts and issues discussed in Web Programming 1 surrounding software development for programs that operate on the web and the Internet. Existing and emerging web development topics to be covered include web applications, web services, enterprise web development, markup languages, and server-side programming.").build(),
                Course.builder().name("Functional Programming").description("An introduction to the principles of typed functional programming. Programming recursive functions over structured data types and informal reasoning by induction about the correctness of those functions. Functional algorithms and data structures. Principles of modular programming, type abstraction, representation invariants and representation independence. Parallel functional programming, algorithms and applications.").build(),
                Course.builder().name("Comparative Programming Languages").description("This course focuses on the organization of programming languages, emphasizing language design concepts and semantics. This course will explore the study of language features and major programming paradigms, with a special emphasis on functional programming.").build(),
                Course.builder().name("Modern Methods of Software Engineering").build(),
                Course.builder().name("Mobile Applications").description("The course explores concepts and issues surrounding information system applications to real-time operating systems and wireless networking systems.").build(),
                Course.builder().name("Advanced Networking and Data Security").description("This course explores the basic components and design principles of advanced broadband networks (wireline and wireless) and how they enable essential services such as mobility, and secure data storage, processing and transmission. This course will also introduce the student to emerging issues facing organizations considering implementing cloud computing services and mobility to enabling worker productivity. Students will also be exposed to the basic pillars of network security (IA) and protecting individual privacy.").build(),
                Course.builder().name("Computer Graphics").description("This course explores graphics applications and systems. Topics to be covered include the basic structure of interactive systems, implementation of packages, distributed architectures for graphics, and the representation of surfaces.").build(),
                Course.builder().name("Data Mining and Machine Learning").description("This course presents an introduction to current concepts in machine learning, knowledge discovery, and data mining. Approaches to the analysis of learning algorithm performance will also be discussed and applied.").build(),
                Course.builder().name("Artificial Intelligence").description("This course will cover current concepts and techniques in artificial intelligence, including “reasoning”, problem solving, and search optimization.").build(),
                Course.builder().name("Robotics").build(),
                Course.builder().name("Introduction to Logic Design").description("Logic & Computation is a Bachelor of Science degree. The curriculum of the major is designed to be flexible and tailored to the individual student’s interests. There are three pre-requisite courses that students will take to prepare them in computer science, mathematics and statistics. The course requirements consist of seven core courses (including the Senior Thesis) and four advanced electives.").build(),
                Course.builder().name("Compiling Techniques").description("Understand the design and construction of compilers. Concepts include syntax analysis, semantics, code generation, optimization and run-time systems. Translation of imperative languages (such as C), functional languages (such as ML), and object-oriented languages (such as Java) will be studied. Students will implement a complete compiler for a small language.").build(),
                Course.builder().name("Introduction to Graph Theory").build(),
                Course.builder().name("Cryptography").description("An introduction to modern cryptography with an emphasis on fundamental ideas. The course will survey both the basic information and complexity-theoretic concepts as well as their (often surprising and counter-intuitive) applications. Among the topics covered will be private key and public key encryption schemes, digital signatures, pseudorandom generators and functions, chosen ciphertext security; and time permitting, some advanced topics such as zero knowledge proofs, secret sharing, private information retrieval, and quantum cryptography.").build(),
                Course.builder().name("Information Retrieval, Discovery, and Delivery").description("This course examines the methods used to gather, organize and search for information in large digital collections (e.g. web search engines). We study classic techniques of indexing documents and searching text and also new algorithms that exploit properties of the Web (e.g. links) social networks and other digital collections, including multimedia collections. Techniques include those for relevance and ranking of documents, exploiting user history, and information clustering. We also examine systems aspects of search technology: how distributed computing and storage are used to make information delivery efficient.").build(),
                Course.builder().name("Neural Networks: Theory and Application").description("Organization of synaptic connectivity as the basis of neural computation and learning. Multilayer perceptrons, convolutional networks, and recurrent networks. Backpropagation and Hebbian learning. Models of perception, language, memory, and neural development.").build(),
                Course.builder().name("Introduction to Analytic Combinatorics").description("Analytic Combinatorics aims to enable precise quantitative predictions of the properties of large combinatorial structures. The theory has emerged over recent decades as essential both for the scientific analysis of algorithms in computer science and for the study of scientific models in many other disciplines. This course combines motivation for the study of the field with an introduction to underlying techniques, by covering as applications the analysis of numerous fundamental algorithms from computer science. The second half of the course introduces Analytic Combinatorics, starting from basic principles.").build(),
                Course.builder().name("Advanced Algorithm Design").description("Gives a broad exposure to algorithmic design ideas of the past few decades, and brings students up to a level where they can understand research papers in algorithms. Although designed for computer science grads, it may be suitable for advanced undergrads and non-CS grads as well.\n" +
                        "\n" +
                        "The course is thematically distinct from undergrad algorithms (such as COS 423) in its extensive use of ideas such as randomness, optimization and approximation, and high dimensional geometry, which are increasingly important in applications. It also introduces other concerns that arise today, such as dealing with uncertainty, big data sizes, and strategic (i.e., game-theoretic) behaviors. All necessary mathematical tools will be covered in class.").build(),
                Course.builder().name("Mathematical Analysis of Algorithms").description("Methods for determining the average-case performance of fundamental algorithms; ordinary and exponential generating functions, real asymptotics, complex asymptotics, singularity analysis, and Mellin transforms; and application to the analysis of Quicksort, hashing, binary tree search, digital search, communication protocols, multidimensional search, set merging, and other combinatorial algorithms. The course is intended to survey the major approaches and applications and to serve as an introduction to research in the field.").build(),
                Course.builder().name("Probabilistic Algorithms").description("Construction and analysis of algorithms that solve various problems efficiently in a probabilistic sense; algorithms that work almost always and for almost all inputs; expected performance of heuristic algorithms; and fundamental limitations on probabilistic computations and other complexity issues.").build(),
                Course.builder().name("Advanced Cryptography").description("This course covers a selection of advanced topics in cryptography, including some or all of the following: fully homomorphic encryption, zero knowledge proofs, traitor tracing, identity-based encryption, private information retrieval, garbled circuits, secret sharing, multiparty computation, lattice-based cryptography, and elliptic curve-based cryptography.").build(),
                Course.builder().name("Great Moments in Computing").description("Course covers pivotal developments in computing, including hardware, software, and theory. Material will be covered by reading seminal papers, patents, and descriptions of highly-influential architectures. Course emphasizes a deep understanding of the discoveries and inventions that brought computer systems to where they are today, and class is discussion-oriented. Final project or paper required. Graduate students and advanced undergraduates from ELE, COS, and related fields welcome.").build(),
                Course.builder().name("Communications and Networking").build())
                .collect(Collectors.toMap(Course::getId, course -> course));
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    public Collection<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public Student getStudent(final UUID id) {
        return students.get(id);
    }

    public Course getCourse(final UUID id) {
        return courses.get(id);
    }

    public void addStudent(final Student student) {
        this.students.put(student.getId(), student);
    }

    public void participate(final Student student, final Course course) {
        course.addParticipant(student);
    }

    public Collection<Course> getCoursesFor(final Student student) {
        return this.courses.values().stream()
                .filter(course -> course.getParticipants().contains(student))
                .collect(Collectors.toList());
    }

    public void terminate(final Student student, final Course course) {
        course.removeParticipant(student);
    }
}
