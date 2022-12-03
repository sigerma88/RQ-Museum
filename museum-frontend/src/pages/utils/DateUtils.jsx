/**
 * Helper function to get the day of the week from a date
 *
 * @param date
 * @returns day of the week
 * @author Victor
 */
export function getDayOfWeek(date) {
  const dayOfWeek = new Date(date).getUTCDay();
  return isNaN(dayOfWeek)
    ? null
    : [
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
      ][dayOfWeek];
}

/**
 * Helper function to get the year from a date
 *
 * @param date
 * @returns year of the date
 * @author Victor
 */
export function getYear(date) {
  const year = new Date(date).getUTCFullYear();
  return isNaN(year) ? null : year;
}

/**
 * Helper function to get the month from a date
 *
 * @param date
 * @returns month of the date
 * @author Victor
 */
export function getMonth(date) {
  const month = new Date(date).getUTCMonth();
  return isNaN(month)
    ? null
    : [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
      ][month];
}

/**
 * Helper function that returns the numerical month of a date
 *
 * @param date
 * @returns numerical month of the date
 * @author Victor
 */
export function getMonthNum(date) {
  const month = new Date(date).getMonth();
  return isNaN(month)
    ? null
    : ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"][month];
}

/**
 * Helper function to get the day from a date
 *
 * @param date
 * @returns day of the date
 * @author Victor
 */
export function getDay(date) {
  let day = new Date(date).getUTCDate();
  return isNaN(day) ? null : day;
}

/**
 * Helper function that returns a formatted date YYYY-MM-DD to feed the JSON object in the POST request
 *
 * @param date
 * @returns formatted date YYYY-MM-DD
 * @author Victor
 */
export function getDate(date) {
  return getYear(date) + "-" + getMonthNum(date) + "-" + getDay(date);
}
