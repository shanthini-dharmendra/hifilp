package com.example.hifilp

object TimeTableData {
    val timeTable = mapOf(
        "Monday" to mapOf(
            "09:00" to "EEAFC",
            "09:50" to "AI",
            "11:00" to "DAD",
            "11:50" to "PSOM",
            "13:40" to "MAD Lab",
            "14:30" to "MAD LAB Continued...",
            "15:20" to "Project II",
            "16:10" to "Skillrack"
        ),
        "Tuesday" to mapOf(
            "09:00" to "Fuzzy",
            "09:50" to "EEAFC",
            "11:00" to "PC[theory]",
            "11:50" to "IR",
            "13:40" to "Project II",
            "14:30" to "Project II",
            "15:20" to "TWM",
            "16:10" to "TWM"
        ),
        "Wednesday" to mapOf(
            "09:00" to "DAD",
            "09:50" to "Fuzzy",
            "11:00" to "IR",
            "11:50" to "AI",
            "13:40" to "PC Lab",
            "14:30" to "PC Lab",
            "15:20" to "Project II",
            "16:10" to "Skillrack"
        ),
        "Thursday" to mapOf(
            "09:00" to "PSOM",
            "09:50" to "AI",
            "11:00" to "MAD[Theory]",
            "11:50" to "EEAFC",
            "13:40" to "MAD LAB",
            "14:30" to "MAD LAB",
            "15:20" to "Project II",
            "16:10" to "Peer Coaching"
        ),
        "Friday" to mapOf(
            "09:00" to "IR",
            "09:50" to "DAD",
            "11:00" to "PSOM",
            "11:50" to "Fuzzy",
            "13:40" to "DS LAB",
            "14:30" to "DS LAB",
            "15:20" to "Project II",
            "16:10" to "Career Guidance"
        ),
        "Saturday" to mapOf(
            "09:00" to "Seminar",
            "09:50" to "Workshop",
            "10:30" to "Project Discussion",
            "11:34" to "Guest Lecture",
            "13:40" to "Self Study",
            "14:30" to "Library",
            "15:20" to "Sports",
            "16:42" to "Skillrack"
        )
        // ✅ No Sunday (Timetable stops on Saturday)
    )

    val subjectFullNames = mapOf(
        "EEAFC" to "Engineering Economics and Accounting Finance",
        "AI" to "Artificial Intelligence",
        "DAD" to "Distributed Application Development",
        "PSOM" to "Privacy and Security in Online Social Media",
        "MAD" to "Mobile Application Development",
        "Fuzzy" to "Fuzzy Systems",
        "PC" to "Professional Communication",
        "IR" to "Information Retrieval",
        "DS LAB" to "Data Science Lab",
        "TWM" to "Tutor Ward Meeting",
        "PC Lab" to "Professional Communication Lab",
        "Peer Coaching" to "Peer Learning Session",
        "Career Guidance" to "Career Development Program",
        "Seminar" to "Technical Seminar",
        "Workshop" to "Skill Development Workshop",
        "Project Discussion" to "Project Planning & Discussion",
        "Guest Lecture" to "Industry Expert Talk",
        "Self Study" to "Independent Learning",
        "Library" to "Library Research",
        "Sports" to "Physical Activities"
    )
}
