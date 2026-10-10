package com.nexus.platform.config;

import com.nexus.platform.entity.Domain;
import com.nexus.platform.entity.Event;
import com.nexus.platform.entity.Opportunity;
import com.nexus.platform.entity.Post;
import com.nexus.platform.entity.Resource;
import com.nexus.platform.entity.Role;
import com.nexus.platform.entity.User;
import com.nexus.platform.repository.EventRepository;
import com.nexus.platform.repository.OpportunityRepository;
import com.nexus.platform.repository.PostRepository;
import com.nexus.platform.repository.ResourceRepository;
import com.nexus.platform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Value("${nexus.seed.demo-users:true}")
    private boolean seedDemoUsers;

    @Bean
    CommandLineRunner seed(
            UserRepository users,
            PostRepository posts,
            EventRepository events,
            ResourceRepository resources,
            OpportunityRepository opportunities,
            PasswordEncoder encoder) {

        return args -> {
            if (users.count() == 0 && seedDemoUsers) {
                users.save(User.builder()
                        .name("NEXUS Admin")
                        .email("admin@nexus.local")
                        .password(encoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .domain(Domain.COMMUNITY)
                        .city("Hyderabad")
                        .build());

                users.save(User.builder()
                        .name("Demo Student")
                        .email("student@nexus.local")
                        .password(encoder.encode("Student@123"))
                        .role(Role.STUDENT)
                        .domain(Domain.EDUCATION)
                        .city("Hyderabad")
                        .build());

                users.save(User.builder()
                        .name("Demo Faculty")
                        .email("faculty@nexus.local")
                        .password(encoder.encode("Faculty@123"))
                        .role(Role.FACULTY)
                        .domain(Domain.EDUCATION)
                        .city("Hyderabad")
                        .build());

                users.save(User.builder()
                        .name("Demo Farmer")
                        .email("farmer@nexus.local")
                        .password(encoder.encode("Farmer@123"))
                        .role(Role.FARMER)
                        .domain(Domain.AGRICULTURE)
                        .city("Hyderabad")
                        .build());
            }

            if (posts.count() == 0) {
                posts.save(Post.builder()
                        .title("Welcome to NEXUS")
                        .content("A unified platform for learning, agriculture, healthcare, business, technology and community collaboration.")
                        .domain(Domain.COMMUNITY)
                        .type("ANNOUNCEMENT")
                        .authorName("NEXUS Admin")
                        .authorEmail("admin@nexus.local")
                        .build());

                posts.save(Post.builder()
                        .title("AI & Full Stack Learning Resources")
                        .content("Explore REST APIs, Spring Boot, React, MySQL, security and AI integration through practical projects.")
                        .domain(Domain.EDUCATION)
                        .type("RESOURCE")
                        .authorName("Demo Faculty")
                        .authorEmail("faculty@nexus.local")
                        .build());
            }

            if (resources.count() == 0) {
                resources.save(Resource.builder()
                        .title("Spring Boot REST API Guide")
                        .description("Learn controllers, service layers, repositories, DTOs, validation and REST API testing.")
                        .tags("spring boot,rest,java,backend")
                        .url("https://spring.io/guides")
                        .domain(Domain.EDUCATION)
                        .uploadedBy("Demo Faculty")
                        .build());

                resources.save(Resource.builder()
                        .title("Smart Farming Basics")
                        .description("Practical concepts for soil, irrigation, crop planning and digital agriculture.")
                        .tags("farming,agriculture,soil,irrigation")
                        .url("https://farmer.gov.in/")
                        .domain(Domain.AGRICULTURE)
                        .uploadedBy("NEXUS Admin")
                        .build());
            }

            if (events.count() == 0) {
                events.save(Event.builder()
                        .title("NEXUS Community Meetup")
                        .description("Cross-domain networking and project collaboration session.")
                        .location("Innovation Lab")
                        .eventDate(LocalDateTime.now().plusDays(7))
                        .domain(Domain.COMMUNITY)
                        .organizer("NEXUS Team")
                        .seats(100)
                        .build());
            }

            if (opportunities.count() == 0) {
                opportunities.save(Opportunity.builder()
                        .title("Full Stack Java Internship")
                        .organization("NEXUS Labs")
                        .type("INTERNSHIP")
                        .location("Hybrid")
                        .applyUrl("https://example.com")
                        .domain(Domain.TECHNOLOGY)
                        .description("Build Spring Boot and React applications.")
                        .deadline(LocalDateTime.now().plusDays(30))
                        .build());
            }

            // Seed a practical cross-domain catalogue so a new NEXUS install is useful on day one.
            if (resources.count() < 10) {
                String[][] catalogue = {
                    {"EDUCATION", "National Digital Library of India", "Search books, articles and learning material from India's digital library network.", "learning,library,books", "https://ndl.iitkgp.ac.in/"},
                    {"EDUCATION", "SWAYAM Online Courses", "Explore free online courses from Indian universities and institutions.", "courses,online,skills", "https://swayam.gov.in/"},
                    {"EDUCATION", "DIKSHA Learning Platform", "Access curriculum-linked learning material and teacher resources.", "school,teacher,learning", "https://diksha.gov.in/"},
                    {"AGRICULTURE", "ICAR Farmer Resources", "Find agricultural research, crop practices and extension information.", "crops,research,farming", "https://icar.org.in/"},
                    {"AGRICULTURE", "Soil Health Card Scheme", "Learn about soil testing and nutrient recommendations for farms.", "soil,crops,government", "https://soilhealth.dac.gov.in/"},
                    {"AGRICULTURE", "eNAM National Agriculture Market", "Explore India's electronic trading network for agricultural produce.", "markets,trade,produce", "https://www.enam.gov.in/"},
                    {"HEALTHCARE", "National Health Mission", "Browse public health programmes and community health information.", "public health,programmes,community", "https://nhm.gov.in/"},
                    {"HEALTHCARE", "Ayushman Bharat PM-JAY", "Check programme information for publicly funded health coverage.", "health,coverage,government", "https://pmjay.gov.in/"},
                    {"HEALTHCARE", "National Health Portal", "Find public health information and service links from the Government of India.", "health,wellness,services", "https://www.nhp.gov.in/"},
                    {"BUSINESS", "Startup India Learning Program", "Explore startup learning material, policy information and ecosystem support.", "startup,business,learning", "https://www.startupindia.gov.in/"},
                    {"BUSINESS", "Udyam Registration", "Read official information for micro, small and medium enterprise registration.", "msme,small business,registration", "https://udyamregistration.gov.in/"},
                    {"BUSINESS", "Skill India Digital Hub", "Discover skilling, employment and entrepreneurship learning opportunities.", "skills,employment,enterprise", "https://www.skillindiadigital.gov.in/"},
                    {"TECHNOLOGY", "MDN Web Development", "Use practical guides for HTML, CSS, JavaScript and web APIs.", "web,javascript,frontend", "https://developer.mozilla.org/"},
                    {"TECHNOLOGY", "Spring Guides", "Build Java applications with official Spring getting-started guides.", "java,spring,backend", "https://spring.io/guides"},
                    {"TECHNOLOGY", "GitHub Skills", "Learn GitHub workflows through hands-on interactive courses.", "git,github,development", "https://skills.github.com/"},
                    {"GOVERNMENT", "MyScheme Benefits Finder", "Find government schemes based on your profile and eligibility.", "schemes,benefits,services", "https://www.myscheme.gov.in/"},
                    {"GOVERNMENT", "National Career Service", "Browse career guidance, job search and employment services.", "careers,jobs,services", "https://www.ncs.gov.in/"},
                    {"GOVERNMENT", "India Portal", "Access public services, government information and citizen resources.", "citizens,services,information", "https://www.india.gov.in/"},
                    {"COMMUNITY", "MyGov India", "Join public consultations, discussions and community participation activities.", "community,consultation,citizens", "https://www.mygov.in/"},
                    {"COMMUNITY", "National Service Scheme", "Explore student volunteering and community service activities.", "volunteer,students,community", "https://nss.gov.in/"}
                };
                for (String[] item : catalogue) {
                    resources.save(Resource.builder().title(item[1]).description(item[2]).tags(item[3]).url(item[4]).domain(Domain.valueOf(item[0])).uploadedBy("NEXUS Community").build());
                }
            }

            if (posts.count() < 8) {
                String[][] updates = {
                    {"EDUCATION", "Start a peer study circle", "Choose one subject, set a weekly goal and invite someone from another course to learn together.", "DISCUSSION"},
                    {"AGRICULTURE", "Share local growing practices", "What crop, soil or water-saving practice has worked well in your area this season?", "QUESTION"},
                    {"HEALTHCARE", "Community health information", "Share links to reliable public health services and awareness resources from trusted providers.", "DISCUSSION"},
                    {"BUSINESS", "Small business peer exchange", "Introduce your local business or idea and share one challenge where peer advice could help.", "DISCUSSION"},
                    {"TECHNOLOGY", "Showcase a learning project", "Share what you built, the tools you used and one lesson that could help another learner.", "DISCUSSION"},
                    {"GOVERNMENT", "Find a public service together", "Have you used a government service or scheme that others in your community should know about? Share its official link.", "QUESTION"},
                    {"COMMUNITY", "Plan a neighbourhood meetup", "Suggest a topic, accessible location and time for a small cross-field community gathering.", "ANNOUNCEMENT"},
                    {"EDUCATION", "Mentorship welcome", "Students and professionals: share a skill you can teach or a topic where you would value guidance.", "DISCUSSION"},
                    {"AGRICULTURE", "Farm knowledge exchange", "Connect growers and learners to discuss sustainable soil and irrigation methods.", "ANNOUNCEMENT"},
                    {"HEALTHCARE", "Wellbeing resource exchange", "Share verified wellbeing resources that are useful and easy for local communities to access.", "RESOURCE"},
                    {"BUSINESS", "Collaboration board", "Find people with complementary skills to work together on a local business or community project.", "DISCUSSION"},
                    {"TECHNOLOGY", "Open learning session", "Propose a short hands-on session where community members can practise a useful digital skill.", "ANNOUNCEMENT"},
                    {"GOVERNMENT", "Help make services easier to find", "Suggest official service links or plain-language guidance that could help people navigate public programmes.", "DISCUSSION"},
                    {"COMMUNITY", "Volunteer skills directory", "Introduce one skill you can contribute to a community project and what kind of project interests you.", "DISCUSSION"}
                };
                for (String[] item : updates) {
                    posts.save(Post.builder().domain(Domain.valueOf(item[0])).title(item[1]).content(item[2]).type(item[3]).authorName("NEXUS Community").authorEmail("community@nexus.local").build());
                }
            }

            if (events.count() < 5) {
                String[][] gatherings = {
                    {"EDUCATION", "Community Study Jam", "Bring a topic you are learning and pair up for a focused study session.", "Online study room"},
                    {"AGRICULTURE", "Sustainable Farming Roundtable", "Growers and learners exchange practical soil and water conservation ideas.", "Community agriculture centre"},
                    {"HEALTHCARE", "Community Wellbeing Workshop", "A public-information session for sharing trusted prevention and wellbeing resources.", "Local community hall"},
                    {"BUSINESS", "Local Enterprise Networking", "Meet peers, exchange ideas and explore practical local collaboration.", "Business resource centre"},
                    {"TECHNOLOGY", "Build Together Workshop", "A beginner-friendly session to plan and build a small useful digital project.", "Innovation Lab"},
                    {"GOVERNMENT", "Citizen Services Help Desk", "Learn where to find official public-service information and assistance.", "Community service centre"},
                    {"COMMUNITY", "NEXUS Neighbourhood Meetup", "An open gathering for people across fields to meet and plan local projects.", "Community hall"}
                };
                int day = 7;
                for (String[] item : gatherings) {
                    events.save(Event.builder().domain(Domain.valueOf(item[0])).title(item[1]).description(item[2]).location(item[3]).eventDate(LocalDateTime.now().plusDays(day++)).organizer("NEXUS Community").seats(40).build());
                }
            }

            if (opportunities.count() < 7) {
                String[][] paths = {
                    {"EDUCATION", "Peer Learning Mentor", "NEXUS Learning Network", "VOLUNTEER", "Online", "Support peer study groups and share learning resources."},
                    {"AGRICULTURE", "Community Farm Project Volunteer", "Local Growers Network", "PROJECT", "Community based", "Help document sustainable growing practices with local growers."},
                    {"HEALTHCARE", "Public Health Outreach Volunteer", "Community Health Network", "VOLUNTEER", "Community based", "Help communities find reliable public health information."},
                    {"BUSINESS", "Small Business Skills Programme", "Startup India", "PROGRAM", "India", "Explore learning and ecosystem support for early-stage entrepreneurs."},
                    {"TECHNOLOGY", "Open Source Project Contributor", "GitHub Skills Community", "PROJECT", "Remote", "Build practical experience by contributing to an open learning project."},
                    {"GOVERNMENT", "Career Guidance and Job Search", "National Career Service", "CAREER", "India", "Browse government career guidance and employment services."},
                    {"COMMUNITY", "Community Action Project", "NEXUS Community", "PROJECT", "Local", "Join neighbours from different fields to develop a useful local initiative."}
                };
                for (String[] item : paths) {
                    opportunities.save(Opportunity.builder().domain(Domain.valueOf(item[0])).title(item[1]).organization(item[2]).type(item[3]).location(item[4]).description(item[5]).applyUrl(item[0].equals("TECHNOLOGY") ? "https://skills.github.com/" : item[0].equals("GOVERNMENT") ? "https://www.ncs.gov.in/" : "https://www.myscheme.gov.in/").deadline(LocalDateTime.now().plusDays(60)).build());
                }
            }

            ensureMinimumContent(posts, events, resources, opportunities);
        };
    }

    private void ensureMinimumContent(PostRepository posts, EventRepository events,
                                      ResourceRepository resources, OpportunityRepository opportunities) {
        String[][] resourceTitles = {
            {"SWAYAM Online Courses", "DIKSHA Learning Platform", "National Digital Library of India", "NPTEL Open Courses", "ePathshala Textbooks", "NCERT Learning Resources", "AICTE Internship Portal", "National Scholarship Portal", "e-Yantra Robotics Learning", "Virtual Labs India"},
            {"ICAR Farmer Resources", "Soil Health Card", "eNAM Agriculture Market", "Kisan Call Centre", "PM Fasal Bima Yojana", "National Horticulture Board", "Agmarknet Market Prices", "Krishi Vigyan Kendra Network", "mKisan Advisory Service", "Agriculture Infrastructure Fund"},
            {"National Health Mission", "Ayushman Bharat PM-JAY", "National Health Portal", "eSanjeevani Telemedicine", "ABHA Health Account", "National AIDS Control Programme", "Food Safety and Nutrition", "Mental Health Support Resources", "Immunization Information", "Pradhan Mantri Bhartiya Janaushadhi"},
            {"Startup India Learning Program", "Udyam Registration", "Skill India Digital Hub", "MSME Champions Portal", "GeM Seller Learning", "SIDBI Business Resources", "Startup India Seed Fund", "NIESBUD Entrepreneurship Courses", "MUDRA Loan Information", "National Small Industries Corporation"},
            {"MDN Web Development", "Spring Guides", "GitHub Skills", "freeCodeCamp Curriculum", "Python Official Tutorial", "Kaggle Learn", "Google Cloud Skills Boost", "Microsoft Learn", "OWASP Web Security", "Linux Foundation Training"},
            {"MyScheme Benefits Finder", "National Career Service", "India Portal", "DigiLocker Services", "UMANG Citizen Services", "CPGRAMS Public Grievances", "RTI Online", "Parivahan Services", "National Scholarship Portal", "Aadhaar Services"},
            {"MyGov India", "National Service Scheme", "UN Volunteers Learning", "Indian Red Cross Society", "Youth Ki Awaaz Community", "NYKS Youth Programmes", "UNICEF India Resources", "Community Radio Network", "Disaster Management Awareness", "Accessible India Campaign"}
        };
        String[][] resourceUrls = {
            {"https://swayam.gov.in/", "https://diksha.gov.in/", "https://ndl.iitkgp.ac.in/", "https://nptel.ac.in/", "https://epathshala.nic.in/", "https://ncert.nic.in/", "https://internship.aicte-india.org/", "https://scholarships.gov.in/", "https://www.e-yantra.org/", "https://www.vlab.co.in/"},
            {"https://icar.org.in/", "https://soilhealth.dac.gov.in/", "https://www.enam.gov.in/", "https://mkisan.gov.in/", "https://pmfby.gov.in/", "https://nhb.gov.in/", "https://agmarknet.gov.in/", "https://icar.org.in/content/krishi-vigyan-kendra", "https://mkisan.gov.in/", "https://agriinfra.dac.gov.in/"},
            {"https://nhm.gov.in/", "https://pmjay.gov.in/", "https://www.nhp.gov.in/", "https://esanjeevani.mohfw.gov.in/", "https://abha.abdm.gov.in/", "https://naco.gov.in/", "https://fssai.gov.in/", "https://telemanas.mohfw.gov.in/", "https://mohfw.gov.in/", "https://janaushadhi.gov.in/"},
            {"https://www.startupindia.gov.in/", "https://udyamregistration.gov.in/", "https://www.skillindiadigital.gov.in/", "https://champions.gov.in/", "https://gem.gov.in/", "https://www.sidbi.in/", "https://seedfund.startupindia.gov.in/", "https://niesbud.nic.in/", "https://www.mudra.org.in/", "https://www.nsic.co.in/"},
            {"https://developer.mozilla.org/", "https://spring.io/guides", "https://skills.github.com/", "https://www.freecodecamp.org/learn/", "https://docs.python.org/3/tutorial/", "https://www.kaggle.com/learn", "https://www.cloudskillsboost.google/", "https://learn.microsoft.com/", "https://owasp.org/www-project-top-ten/", "https://training.linuxfoundation.org/"},
            {"https://www.myscheme.gov.in/", "https://www.ncs.gov.in/", "https://www.india.gov.in/", "https://www.digilocker.gov.in/", "https://web.umang.gov.in/", "https://pgportal.gov.in/", "https://rtionline.gov.in/", "https://parivahan.gov.in/", "https://scholarships.gov.in/", "https://uidai.gov.in/"},
            {"https://www.mygov.in/", "https://nss.gov.in/", "https://www.unv.org/", "https://indianredcross.org/", "https://www.youthkiawaaz.com/", "https://nyks.nic.in/", "https://www.unicef.org/india/", "https://mib.gov.in/", "https://ndma.gov.in/", "https://depwd.gov.in/"}
        };
        String[][] opportunityTitles = {
            {"Peer Study Circle Facilitator", "Student Research Project Assistant", "Community Tutoring Volunteer", "Education Content Reviewer", "Learning Resource Curator"},
            {"Sustainable Farm Pilot Volunteer", "Soil Sampling Project Assistant", "Crop Advisory Outreach Helper", "Farm Market Data Contributor", "Community Garden Coordinator"},
            {"Health Awareness Outreach Volunteer", "Community Wellness Resource Curator", "Public Health Survey Assistant", "Wellbeing Session Coordinator", "Health Access Information Helper"},
            {"Small Business Growth Project", "Local Enterprise Mentor Volunteer", "Market Research Assistant", "Startup Pitch Practice Cohort", "MSME Digital Skills Project"},
            {"Open Source Contributor Cohort", "Web Development Project Mentor", "Digital Skills Workshop Assistant", "Cyber Safety Awareness Project", "Community Data Dashboard Project"},
            {"Citizen Services Guide Volunteer", "Public Scheme Information Assistant", "Community Digital Access Project", "Local Career Help Desk Volunteer", "Public Feedback Research Project"},
            {"Neighbourhood Improvement Project", "Community Volunteer Coordinator", "Local Skills Exchange Project", "Accessible Events Volunteer", "Youth Community Action Cohort"}
        };
        String[][] eventTitles = {
            {"Community Study Circle", "Scholarship Search Workshop", "Student Project Showcase", "Peer Mentoring Session", "Digital Learning Orientation"},
            {"Soil Health Learning Clinic", "Water-Smart Farming Workshop", "Grower Knowledge Exchange", "Farm Market Readiness Session", "Community Seed and Crop Meet"},
            {"Community Wellness Workshop", "Public Health Information Session", "First Aid Awareness Meetup", "Healthy Living Community Forum", "Health Services Access Clinic"},
            {"Small Business Peer Meetup", "Startup Idea Feedback Session", "Digital Tools for Local Business", "Entrepreneur Resource Clinic", "Local Market Connections Forum"},
            {"Build a Web Project Workshop", "Open Source Contributor Meetup", "Cyber Safety Basics Session", "Community Coding Lab", "Digital Skills Practice Hour"},
            {"Citizen Services Help Session", "Government Scheme Information Clinic", "Digital Public Services Workshop", "Career Services Orientation", "Community Feedback Roundtable"},
            {"Neighbourhood Ideas Meetup", "Community Volunteer Welcome", "Local Skills Sharing Circle", "Inclusive Community Planning Session", "Community Project Showcase"}
        };
        String[][] postTitles = {
            {"Find a study partner", "Share a learning resource", "Ask an education mentor"},
            {"Share a local farming practice", "Ask growers about water saving", "Find an agriculture project partner"},
            {"Share a trusted health resource", "Ask about community wellbeing", "Find volunteers for health awareness"},
            {"Introduce a local business idea", "Ask peers for business feedback", "Find a partner for a small project"},
            {"Share a digital project", "Ask for help with a coding challenge", "Find collaborators for an open project"},
            {"Share an official public service link", "Ask how to access a public scheme", "Suggest a service navigation guide"},
            {"Share a neighbourhood need", "Invite volunteers for a local idea", "Offer a skill to the community"}
        };
        Domain[] fields = Domain.values();
        for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
            Domain domain = fields[fieldIndex];
            int resourceCount = (int) resources.countByDomain(domain);
            for (int i = resourceCount; i < 10; i++) {
                String title = resourceTitles[fieldIndex][i];
                resources.save(Resource.builder().domain(domain).title(title)
                        .description("Official or established " + domain.name().toLowerCase() + " learning and service resource. Review eligibility, current guidance and terms on the provider website.")
                        .tags(domain.name().toLowerCase() + ", learning, services, India")
                        .url(resourceUrls[fieldIndex][i]).uploadedBy("NEXUS Resource Library").build());
            }
            int opportunityCount = (int) opportunities.countByDomain(domain);
            for (int i = opportunityCount; i < 5; i++) {
                String[] opportunityTypes = {"PROJECT", "VOLUNTEER", "PROGRAM", "INTERNSHIP", "CAREER"};
                opportunities.save(Opportunity.builder().domain(domain).title(opportunityTitles[fieldIndex][i])
                        .organization("NEXUS Community Network").type(opportunityTypes[i]).location("Online / local community")
                        .description("Community starter opportunity for people interested in " + domain.name().toLowerCase() + ". Connect with the organizer to confirm current availability, requirements and next steps.")
                        .applyUrl(resourceUrls[fieldIndex][i]).deadline(LocalDateTime.now().plusDays(45 + i)).build());
            }
            int eventCount = (int) events.countByDomain(domain);
            for (int i = eventCount; i < 5; i++) {
                LocalDateTime date = LocalDateTime.now().plusDays(7 + fieldIndex * 5L + i).with(LocalTime.of(10 + i, 0));
                events.save(Event.builder().domain(domain).title(eventTitles[fieldIndex][i])
                        .description("NEXUS community session for learning, practical questions and connections in " + domain.name().toLowerCase() + ". Check with the organizer for the latest schedule.")
                        .location("Online / NEXUS Community").eventDate(date).organizer("NEXUS Community").seats(40)
                        .registrationFee(BigDecimal.ZERO).build());
            }
            int postCount = (int) posts.countByDomain(domain);
            for (int i = postCount; i < 3; i++) {
                posts.save(Post.builder().domain(domain).title(postTitles[fieldIndex][i])
                        .content("Start a conversation with the NEXUS community. Share useful context, a question or a resource related to " + domain.name().toLowerCase() + ".")
                        .type(i == 1 ? "QUESTION" : "DISCUSSION").authorName("NEXUS Community")
                        .authorEmail("community@nexus.local").build());
            }
        }
    }
}
