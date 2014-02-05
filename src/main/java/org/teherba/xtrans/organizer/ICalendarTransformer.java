/*  Transforms iCalendar calendars and schedules
    @(#) $Id: ICalendarTransformer.java 566 2010-10-19 16:32:04Z gfis $
    2007-10-12: extends OrganizerTransformer
    2006-11-04, Dr. Georg Fischer
    
    caution, must be stored as UTF-8 (äöüÄÖÜß)
*/
/*
 * Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.teherba.xtrans.organizer;
import  org.teherba.xtrans.organizer.OrganizerTransformer;

/**	Transformer for iCalendar calendars and schedules.
 *  iCalendar is an ASCII-based format for the description of entries in
 *  a calendar or schedule, and is described in 
 *  <a href="http://www.ietf.org/rfc/rfc2445.txt">RFC 2445</a>.
 *  Each line starts with a key, a colon, and the value.
 *  Examples:
 * <pre>
BEGIN:VCALENDAR
VERSION:2.0
PRODID:-//hacksw/handcal//NONSGML v1.0//EN
BEGIN:VEVENT
DTSTART:19970714T170000Z
DTEND:19970715T035959Z
SUMMARY:Bastille Day Party
END:VEVENT
END:VCALENDAR

BEGIN:VCALENDAR
PRODID:-//RDU Software//NONSGML HandCal//EN
VERSION:2.0
BEGIN:VTIMEZONE
TZID:US-Eastern
BEGIN:STANDARD
DTSTART:19981025T020000
RDATE:19981025T020000
TZOFFSETFROM:-0400
TZOFFSETTO:-0500
TZNAME:EST
END:STANDARD
BEGIN:DAYLIGHT
DTSTART:19990404T020000
RDATE:19990404T020000
TZOFFSETFROM:-0500
TZOFFSETTO:-0400
TZNAME:EDT
END:DAYLIGHT
END:VTIMEZONE
BEGIN:VEVENT
DTSTAMP:19980309T231000Z
UID:guid-1.host1.com
ORGANIZER;ROLE=CHAIR:MAILTO:mrbig@host.com
ATTENDEE;RSVP=TRUE;ROLE=REQ-PARTICIPANT;CUTYPE=GROUP:
 MAILTO:employee-A@host.com
DESCRIPTION:Project XYZ Review Meeting
CATEGORIES:MEETING
CLASS:PUBLIC
CREATED:19980309T130000Z
SUMMARY:XYZ Project Review
DTSTART;TZID=US-Eastern:19980312T083000
DTEND;TZID=US-Eastern:19980312T093000
LOCATION:1CP Conference Room 4350
END:VEVENT
END:VCALENDAR
 * </pre>
 *  @author Dr. Georg Fischer
 */
public class ICalendarTransformer extends OrganizerTransformer { 
    public final static String CVSID = "@(#) $Id: ICalendarTransformer.java 566 2010-10-19 16:32:04Z gfis $";

    /** Constructor.
     */
    public ICalendarTransformer() {
        super();
        setFormatCodes		("ical");
        setDescription		("Calendars and schedules");
        setFileExtensions	("ics,ifb,iCal,iFBf");
        setMimeType			("text/calendar");
    } // Constructor
    
	/** Initializes the (quasi-constant) global structures and variables.
	 *  This method is called by the {@link org.teherba.xtrans.XtransFactory} once for the
	 *  selected generator and serializer.
	 */
	public void initialize() {
		super.initialize();
	} // initialize
    
} // ICalendarTransformer
