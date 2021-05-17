import discord
import os
from keep_alive import keep_alive

client = discord.Client()

@client.event
async def on_ready():
  print("We have logged in as {0.user}".format(client))

@client.event
async def on_message(message):
  if message.author == client.user:
    return

  #When someone types $hello and it's not the bot the bot responds with Hello!
  if message.content.startswith("$hello"):
    await message.channel.send("Hello!")
    print("Printed Hello")

keep_alive()
client.run('ODQxNjkwMjcwOTQwMjY2NTM3.YJqbJg.WFiZDhPFi1LF0z-idVy500Bl5Nk')