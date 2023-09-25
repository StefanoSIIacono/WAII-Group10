// Api

export type ApiResponse<T> = {
  success: boolean;
  statusCode: number;
  error?: string;
  data?: T;
};

export type PaginationInfo = {
  page?: number;
  offset?: number;
};

export type ErrorType = {
  errorTitle: string;
  errorDescription: string;
  errorCode: string;
};

// DTOs

export enum Roles {
  PROFILE,
  EXPERT,
  MANAGER
}

export enum Priority {
  TOASSIGN,
  LOW,
  MEDIUM,
  HIGH
}

export enum Status {
  IN_PROGRESS,
  REOPENED,
  OPEN,
  RESOLVED,
  CLOSED
}

export const validStatusChanges = {
  [Status.OPEN]: [Status.RESOLVED, Status.CLOSED, Status.IN_PROGRESS],
  [Status.RESOLVED]: [Status.CLOSED, Status.REOPENED],
  [Status.CLOSED]: [Status.REOPENED],
  [Status.IN_PROGRESS]: [Status.OPEN, Status.CLOSED, Status.RESOLVED],
  [Status.REOPENED]: [Status.IN_PROGRESS, Status.CLOSED, Status.RESOLVED]
};

export type PagedDTO<T> = {
  meta: {
    currentPage: number;
    totalPages: number;
    totalElements: number;
  };
  data: T[];
};

export type ExpertiseDTO = {
  field: string;
};

export type ExpertDTO = {
  email: string;
  name: string;
  surname: string;
  expertises: ExpertiseDTO[];
};

export type StatsDTO = {
  expertEmail: string;
  ticketInProgress: number;
  totalAssignedEver: number;
  totalClosed: number;
  totalTimeToSolveTickets: number;
  closedPerDay: Chart1DataDTO[];
  closedPerExpertise: Chart2DataDTO[];
};

export type Chart1DataDTO = {
  data: string;
  nticketClosed: number;
};

export type Chart2DataDTO = {
  expertise: ExpertiseDTO;
  nticketClosed: number;
};

export type LoginDTO = {
  username: string;
  password: string;
};

export type TokenDTO = {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
};

export type AddressDTO = {
  city: string;
  country: string;
  zipCode: string;
  address: string;
};

export type CreateProfileDTO = {
  email: string;
  password: string;
  name: string;
  surname: string;
  address: AddressDTO;
};

export type CreateExpertDTO = {
  email: string;
  password: string;
  name: string;
  surname: string;
  expertises: string[];
};

export type MeDTOFromApi = {
  email: string;
  role: string;
  name: string;
  surname: string;
  expertises?: ExpertiseDTO[];
  address?: AddressDTO;
};

export type MeDTO = {
  email: string;
  role: Roles;
  name: string;
  surname: string;
  expertises?: ExpertiseDTO[];
  address?: AddressDTO;
};

export type MessageDTO = {
  id: number;
  index: number;
  timestamp: string;
  body: string;
  attachments: AttachmentDTO[];
  expert?: ExpertDTO;
  ticket: number;
};

export type AttachmentDTO = {
  id: number;
  attachment: string;
  size: number;
  contentType: string;
  message: number;
};

export type UnreadMessagesDTO = {
  ticket: number;
  lastReadIndex: number;
  unreadMessagesNumber: number;
};

export type AttachmentBodyDTO = {
  attachment: string;
  contentType: string;
};

export type BodyMessageDTO = {
  body: string;
  attachments: AttachmentBodyDTO[];
};

export type ProductDTO = {
  productId: string;
  name: string;
  brand: string;
};

export type ProfileDTO = {
  email: string;
  name: string;
  surname: string;
  address: AddressDTO;
};

export type ChangeProfileInfoDTO = {
  name?: string;
  surname?: string;
  address?: AddressDTO;
};

export type TicketStatusDTO = {
  status: keyof typeof Status;
  timestamp: string;
  statusChanger: Roles;
  expert?: string;
};

export type TicketDTO = {
  id: number;
  obj: string;
  arg: ExpertiseDTO;
  priority?: Priority;
  profile: ProfileDTO;
  creationDate: string;
  expert?: ExpertDTO;
  product: ProductDTO;
  status: TicketStatusDTO;
  lastReadMessageIndex?: number;
  totalMessages?: number;
};

export type TicketCreateBodyDTO = {
  obj: string;
  arg: string;
  body: string;
  attachments: AttachmentBodyDTO[];
  product: string;
};

export type TicketInProgressBodyDTO = {
  expert: string;
  priority: Priority;
};
