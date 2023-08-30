import { Link } from "../types/link"

export const findLinkByRel = (links: Link[], rel: string): Link | undefined => {

  return links.find(link => link.rel === rel)

}