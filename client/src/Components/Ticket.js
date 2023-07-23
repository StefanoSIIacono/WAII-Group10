function Ticket(ticketId, object, argument, expertId, productId, priority, ticketStatus, messages) {
    this.ticketId = ticketId;
    this.object = object;
    this.argument = argument;
    this.expertId = expertId;
    this.productId = productId;
    this.priority = priority
    this.ticketStatus = ticketStatus;
    this.messages = messages;

}

exports.Ticket = Ticket;
