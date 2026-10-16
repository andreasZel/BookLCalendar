#!/usr/bin/env node
// GitHub issues and pull requests, token G_TOKEN from .env
import { readFileSync, existsSync } from 'node:fs';
import { execSync } from 'node:child_process';

const usage = `Usage:
  node scripts/github.mjs issue list [--state open|closed|all]
  node scripts/github.mjs issue view <number>
  node scripts/github.mjs issue create --title <t> [--body <b> | --body-file <path>] [--label <l>]...
  node scripts/github.mjs issue edit <number> [--title <t>] [--body <b> | --body-file <path>]
  node scripts/github.mjs issue close <number> [--comment <c>]
  node scripts/github.mjs issue comment <number> --body <b>
  node scripts/github.mjs pr list [--state open|closed|all]
  node scripts/github.mjs pr create --title <t> [--body <b> | --body-file <path>] [--head <branch>] [--base <branch>] [--draft]`;

const loadToken = () => {
    if (process.env.G_TOKEN) return process.env.G_TOKEN;
    if (!existsSync('.env')) return null;
    const line = readFileSync('.env', 'utf8').split(/\r?\n/).find((l) => l.trim().startsWith('G_TOKEN='));
    return line ? line.slice(line.indexOf('=') + 1).trim().replace(/^["']|["']$/g, '') : null;
};

const repoSlug = () => {
    const url = execSync('git remote get-url origin', { encoding: 'utf8' }).trim();
    const match = url.match(/github\.com[:/]([^/]+)\/(.+?)(\.git)?$/);
    if (!match) throw new Error(`Cannot read owner/repo from ${url}`);
    return `${match[1]}/${match[2]}`;
};

const parseArgs = (args) => {
    const flags = { label: [] };
    const positional = [];
    for (let i = 0; i < args.length; i++) {
        const arg = args[i];
        if (!arg.startsWith('--')) { positional.push(arg); continue; }
        const key = arg.slice(2);
        if (key === 'draft') { flags.draft = true; continue; }
        const value = args[++i];
        if (value === undefined) throw new Error(`Missing value for --${key}`);
        if (key === 'label') flags.label.push(value); else flags[key] = value;
    }
    return { flags, positional };
};

const bodyOf = (flags) => flags['body-file'] ? readFileSync(flags['body-file'], 'utf8') : flags.body;

const api = async (token, method, path, body) => {
    const res = await fetch(`https://api.github.com${path}`, { method, headers: { Authorization: `Bearer ${token}`, Accept: 'application/vnd.github+json', 'X-GitHub-Api-Version': '2022-11-28', 'User-Agent': 'booklcalendar-scripts' }, body: body ? JSON.stringify(body) : undefined });
    const text = await res.text();
    const data = text ? JSON.parse(text) : null;
    if (!res.ok) throw new Error(`${method} ${path} -> ${res.status}: ${data?.message ?? text}`);
    return data;
};

const main = async () => {
    const [resource, action, ...rest] = process.argv.slice(2);
    if (!resource || !action) { console.log(usage); process.exit(1); }
    const token = loadToken();
    if (!token) throw new Error('G_TOKEN not found in environment or .env');
    const repo = `/repos/${repoSlug()}`;
    const { flags, positional } = parseArgs(rest);
    const number = positional[0];
    const key = `${resource} ${action}`;

    if (key === 'issue list') {
        const items = await api(token, 'GET', `${repo}/issues?state=${flags.state ?? 'open'}&per_page=100`);
        items.filter((i) => !i.pull_request).forEach((i) => console.log(`#${i.number}\t${i.state}\t${i.title}`));
    } else if (key === 'issue view') {
        const i = await api(token, 'GET', `${repo}/issues/${number}`);
        console.log(`#${i.number} ${i.title} [${i.state}]\n${i.html_url}\n\n${i.body ?? ''}`);
    } else if (key === 'issue create') {
        if (!flags.title) throw new Error('--title is required');
        const i = await api(token, 'POST', `${repo}/issues`, { title: flags.title, body: bodyOf(flags), labels: flags.label });
        console.log(`#${i.number}\t${i.html_url}`);
    } else if (key === 'issue edit') {
        const patch = {};
        if (flags.title) patch.title = flags.title;
        const body = bodyOf(flags);
        if (body !== undefined) patch.body = body;
        const i = await api(token, 'PATCH', `${repo}/issues/${number}`, patch);
        console.log(`#${i.number}\t${i.html_url}`);
    } else if (key === 'issue close') {
        if (flags.comment) await api(token, 'POST', `${repo}/issues/${number}/comments`, { body: flags.comment });
        const i = await api(token, 'PATCH', `${repo}/issues/${number}`, { state: 'closed' });
        console.log(`#${i.number}\tclosed`);
    } else if (key === 'issue comment') {
        const c = await api(token, 'POST', `${repo}/issues/${number}/comments`, { body: bodyOf(flags) });
        console.log(c.html_url);
    } else if (key === 'pr list') {
        const items = await api(token, 'GET', `${repo}/pulls?state=${flags.state ?? 'open'}&per_page=100`);
        items.forEach((p) => console.log(`#${p.number}\t${p.state}\t${p.head.ref} -> ${p.base.ref}\t${p.title}`));
    } else if (key === 'pr create') {
        if (!flags.title) throw new Error('--title is required');
        const head = flags.head ?? execSync('git branch --show-current', { encoding: 'utf8' }).trim();
        const p = await api(token, 'POST', `${repo}/pulls`, { title: flags.title, body: bodyOf(flags), head, base: flags.base ?? 'master', draft: !!flags.draft });
        console.log(`#${p.number}\t${p.html_url}`);
    } else {
        console.log(usage);
        process.exit(1);
    }
};

main().catch((e) => { console.error(e.message); process.exit(1); });
