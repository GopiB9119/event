const allowedParameters = ["eventId", "expiry", "checksum", "signature", "title", "creatorEmail", "private"];
const sourceParameters = new URLSearchParams(window.location.search);
const appParameters = new URLSearchParams();

allowedParameters.forEach((name) => {
  const value = sourceParameters.get(name);
  if (value !== null) appParameters.set(name, value);
});

const title = sourceParameters.get("title")?.trim() || "Shared event";
const organizer = sourceParameters.get("creatorEmail")?.trim();
const expiry = Number(sourceParameters.get("expiry"));
const required = ["eventId", "expiry"].every((name) => sourceParameters.has(name)) &&
  (sourceParameters.has("checksum") || sourceParameters.has("signature"));
const expired = Number.isFinite(expiry) && expiry > 0 && Date.now() > expiry;

document.querySelector("#event-title").textContent = title;
document.querySelector("#event-meta").textContent = organizer
  ? `Shared by local organizer label ${organizer}`
  : "Shared from another Community Ledger installation";

const openButton = document.querySelector("#open-app");
const status = document.querySelector("#link-status");

if (!required) {
  openButton.removeAttribute("href");
  openButton.setAttribute("aria-disabled", "true");
  status.textContent = "This event-copy link is missing required fields. Ask the sender for a new link.";
} else if (expired) {
  openButton.removeAttribute("href");
  openButton.setAttribute("aria-disabled", "true");
  status.textContent = "This event-copy link has expired. Ask the sender for a new link.";
} else {
  openButton.href = `communityledger://join?${appParameters.toString()}`;
  status.textContent = "Community Ledger must already be installed to open this event copy.";
}
